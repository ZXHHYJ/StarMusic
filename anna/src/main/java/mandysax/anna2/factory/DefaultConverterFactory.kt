package mandysax.anna2.factory

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value
import mandysax.anna2.utils.jsonParsing
import org.jetbrains.annotations.Contract
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * @author liuxiaoliu66
 */
class DefaultConverterFactory private constructor() : ConverterFactory.Factory {

    override fun <T : Any> create(clazz: Class<T>, json: String?): T? {
        json ?: return null
        val pathMap = HashMap<String, String>()
        val obj: T = try {
            clazz.newInstance()
        } catch (e: IllegalAccessException) {
            throw IllegalAccessException(e.message)
        } catch (e: InstantiationException) {
            throw InstantiationException(e.message)
        }
        for (field in obj.javaClass.declaredFields) {
            if (field.isAnnotationPresent(Path::class.java)) {
                val path = field.getAnnotation(Path::class.java)
                pathMap[field.name] = json
                    .jsonParsing(
                        *(path?.value?.split("/")?.toTypedArray())!!
                    )
            }
        }
        for (field in obj.javaClass.declaredFields) {
            if (field.annotations.isNotEmpty()) {
                val s = pathMap[field.name] ?: json
                val name: String = field.getAnnotation(Value::class.java)?.value ?: field.name
                if (!field.isAccessible) field.isAccessible = true
                when (field.type) {
                    Boolean::class.java -> field.set(obj, parsingBoolean(name, s))
                    String::class.java -> field.set(obj, parsingString(name, s))
                    Int::class.javaPrimitiveType -> field.set(obj, parsingInt(name, s))
                    Long::class.javaPrimitiveType -> field.set(obj, parsingLong(name, s))
                    List::class.java -> {
                        val genericsType = field.getListGenericTypeClass()
                        val array = parsingJsonArray(name, s) ?: continue
                        val list = ArrayList<Any?>()
                        for (i in 0 until array.length()) {
                            try {
                                list.add(create(genericsType, array.getString(i)))
                            } catch (ignored: JSONException) {
                            }
                        }
                        field.set(obj, list)
                    }
                    else -> field.set(obj, parsingObject(field, s))
                }
            }
        }
        return obj
    }

    override fun parsingType(clazz: Class<*>): Boolean = true

    private fun getNextValue(`in`: String?): JSONObject {
        return try {
            if (JSONTokener(`in`).nextValue() is JSONObject) JSONObject(`in`!!) else JSONArray(`in`).optJSONObject(
                0
            )
        } catch (e: JSONException) {
            JSONObject()
        }
    }

    private fun parsingBoolean(name: String, json: String?): Boolean {
        return getNextValue(json).optBoolean(name)
    }

    private fun parsingInt(name: String, json: String?): Int {
        return getNextValue(json).optInt(name)
    }

    private fun parsingLong(name: String, json: String?): Long {
        return getNextValue(json).optLong(name)
    }

    private fun parsingString(name: String, json: String?): String {
        return getNextValue(json).optString(name)
    }

    private fun parsingJsonArray(name: String, json: String?): JSONArray? {
        return getNextValue(json).optJSONArray(name)
    }

    private fun parsingObject(field: Field, json: String?): Any? {
        return create(field.type, json)
    }

    private fun Field.getListGenericTypeClass(): Class<*> {
        val genericsFieldType: Type = this.genericType
        if (genericsFieldType is ParameterizedType) {
            val fieldArgTypes: Array<Type> = genericsFieldType.actualTypeArguments
            for (fieldArgType in fieldArgTypes) {
                return fieldArgType as Class<*>
            }
        }
        throw RuntimeException("Cannot find the generic type of this " + this.name + " field")
    }

    companion object {
        @Contract(" -> new")
        @JvmStatic
        fun create(): DefaultConverterFactory {
            return DefaultConverterFactory()
        }
    }
}