package mandysax.anna2.factory

import mandysax.anna2.Anna2
import mandysax.anna2.TYPE
import mandysax.anna2.annotation.*
import mandysax.anna2.observable.Observable
import okhttp3.FormBody
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

class DefaultCallAdapterFactory : CallAdapterFactory.Factory<Observable<*>> {

    private val mQueryMap = HashMap<String, String>()
    private val mHeaderMap = HashMap<String, String>()
    private val mFromBodyMap = HashMap<String, String>()
    private val mRequestBodyMap = ArrayList<RequestBody>()

    override fun create(anna2: Anna2, method: Method, args: Array<Any?>?): Observable<*> {
        var path: String? = null
        var url: String? = null
        var type: TYPE? = null
        for (annotation in method.annotations) {
            if (type != null && path != null) break
            when (annotation) {
                /*is MultiPart -> {

                }
                is FormUrlEncoded -> {

                }*/
                is Path -> {
                    path = annotation.value
                }
                is Get -> {
                    url = anna2.baseUrl + annotation.value
                    type = TYPE.GET
                }
                is Post -> {
                    url = anna2.baseUrl + annotation.value
                    type = TYPE.POST
                }
                is Delete -> {
                    url = anna2.baseUrl + annotation.value
                    type = TYPE.DELETE
                }
                is Put -> {
                    url = anna2.baseUrl + annotation.value
                    type = TYPE.PUT
                }
            }
        }
        type ?: throw NullPointerException("No annotation request type,For example @Get(...)")
        if (args != null) {
            handlerAnnotation(method, *args)
        }
        val mBuilder: Request.Builder = Request.Builder()
        mBuilder.url(getUrl(url!!, mQueryMap))
        mHeaderMap.run {
            mBuilder.headers(toHeaders())
        }
        when (type) {
            TYPE.GET -> mBuilder.get()
            TYPE.POST -> {
                for (body in mRequestBodyMap)
                    mBuilder.post(body)
                mBuilder.post(formBodyBuild())
            }
            TYPE.DELETE -> {
                for (body in mRequestBodyMap)
                    mBuilder.delete(body)
                mBuilder.delete(formBodyBuild())
            }
            TYPE.PUT -> {
                for (body in mRequestBodyMap)
                    mBuilder.put(body)
                mBuilder.put(formBodyBuild())
            }
        }
        return ObservableFactory(
            OkHttpClient().newCall(mBuilder.build()),
            anna2.converterFactory,
            method.getGenericType()
                ?: throw IllegalStateException("Observable must have generic type (e.g., Observable<ResponseBody>)"),
            path
        )
    }

    private fun getUrl(path: String, paramsMap: Map<String, String>?): String {
        paramsMap ?: return path
        val pathBuilder = StringBuilder("$path?")
        for ((key, value) in paramsMap) {
            pathBuilder.append(key).append("=").append(value).append("&")
        }
        return pathBuilder.substring(0, pathBuilder.length - 1)
    }

    /**
     * @param method 方法
     * @param params 对应参数列表
     */
    @Suppress("UNCHECKED_CAST")
    private fun handlerAnnotation(method: Method, vararg params: Any?) {
        val annotations = method.parameterAnnotations
        for (i in annotations.indices) {
            val s = if (params[i] == null) "null" else {
                when (params[i]) {
                    is RequestBody -> ""
                    is List<*> -> {
                        val s = params[i].toString()
                        if (params[i] is List<*> && s.isNotEmpty() && s[0] == '[' && s.endsWith("]")) {
                            s.substring(1, s.length - 1)
                        } else s
                    }
                    else -> params[i].toString()
                }
            }
            when (annotations[i][0]) {
                is Query -> {
                    val query = annotations[i][0] as Query
                    mQueryMap.add(query.value, s, params[i])
                }
                is Body -> {
                    mRequestBodyMap.add(params[i] as RequestBody)
                }
                is Header -> {
                    val header = annotations[i][0] as Header
                    mHeaderMap.add(header.value, s, params[i])
                }
                is FormData -> {
                    val formData = annotations[i][0] as FormData
                    mFromBodyMap.add(formData.value, s, params[i])
                }
            }
        }
    }

    private fun formBodyBuild(): FormBody {
        val formBody = FormBody.Builder()
        for ((key, value) in mFromBodyMap) {
            formBody.add(key, value)
        }
        return formBody.build()
    }

    private fun HashMap<String, String>.add(key: String, value: String, params: Any?) {
        if (params is HashMap<*, *>) {
            params.run {
                for (kv in this.entries)
                    put(kv.key.toString(), kv.value.toString())
            }
            return
        }
        put(key, value)
    }

    private fun Method.getGenericType(): Class<*>? {
        val type = this.genericReturnType
        if (type is ParameterizedType) {
            if (type.actualTypeArguments.isNotEmpty()) {
                type.actualTypeArguments[0].run {
                    if (this is Class<*>) {
                        return this
                    } else {
                        if (this is ParameterizedType && actualTypeArguments.isNotEmpty())
                            return actualTypeArguments[0] as Class<*>
                    }
                }
            }
        }
        return null
    }

    override fun parsingType(clazz: Class<*>): Boolean = true
}