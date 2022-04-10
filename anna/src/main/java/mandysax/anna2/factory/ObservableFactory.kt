package mandysax.anna2.factory

import android.os.Handler
import android.os.Looper
import mandysax.anna2.Anna2
import mandysax.anna2.callback.Callback
import mandysax.anna2.observable.Observable
import mandysax.anna2.utils.jsonParsing
import okhttp3.Call
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

/**
 * @author liuxiaoliu66
 */
internal class ObservableFactory<T : Any> constructor(
    private val call: Call,
    private val factory: ConverterFactory,
    private val model: Class<T>,
    private val path: String?
) : Observable<T> {

    private val mPath: Array<String>? = path?.split("/")?.toTypedArray()

    companion object {
        @JvmStatic
        private val mHandler = Handler(Looper.getMainLooper())
    }

    private fun run(response: Response, callback: Callback<T>) {
        if (!response.isSuccessful) {
            mHandler.post {
                callback.onFailure(response.code)
            }
            return
        }
        val json: String = if (response.body != null) {
            val s = response.body!!.string()
            if (mPath != null)
                s.jsonParsing(*mPath)
            else
                s
        } else {
            mHandler.post {
                callback.onResponse(null)
            }
            return
        }
        try {
            val method = callback.javaClass.getMethod("onResponse", List::class.java)
            val jsonArray: JSONArray = try {
                JSONArray(json)
            } catch (e: JSONException) {
                mHandler.post {
                    method.invoke(callback, ArrayList<T>())
                }
                return
            }
            mHandler.post {
                val list = ArrayList<T>()
                for (i in 0 until jsonArray.length())
                    factory.create(model, jsonArray.getString(i))?.let {
                        list.add(it)
                    }
                method.invoke(callback, list)
            }
        } catch (e: Exception) {
            mHandler.post {
                callback.onResponse(factory.create(model, json))
            }
        }
    }

    override fun cancel() {
        if (!call.isCanceled()) {
            call.cancel()
        }
    }

    override fun clone(): Observable<T> {
        return ObservableFactory(
            call.clone(),
            factory,
            model,
            path
        )
    }

    override fun set(callback: Callback<T>) {
        call.enqueue(object : okhttp3.Callback {

            override fun onResponse(call: Call, response: Response) {
                run(response, callback)
            }

            override fun onFailure(call: Call, e: IOException) {
                mHandler.post {
                    callback.onFailure(Anna2.UNKNOWN)
                }
            }

        })
    }
}