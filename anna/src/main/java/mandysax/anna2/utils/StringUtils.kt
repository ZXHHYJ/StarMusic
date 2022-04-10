package mandysax.anna2.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

/**
 * @author liuxiaoliu66
 */
internal fun String.jsonParsing(vararg key: String?): String {
    var s = this
    for (name in key) {
        try {
            s =
                if (JSONTokener(s).nextValue() is JSONObject) JSONObject(s).optString(
                    name
                ) else JSONArray(s).optJSONObject(0).optString(name)
        } catch (e: JSONException) {
            return s
        }
    }
    return s
}