package id.bni46.microatmsdk.data

import androidx.annotation.Keep
import com.google.gson.Gson

@Keep
data class MethodResult(val message: String, val data: Any?) {
    fun toJson(): String? {
        return Gson().toJson(this)
    }
}
