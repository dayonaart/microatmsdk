package id.bni46.microatmsdk.data

import androidx.annotation.Keep
import com.google.gson.Gson

@Keep
data class ProgressMessage(val message: String, val hasProgress: Boolean = true) {
    fun toJson(): String? {
        return Gson().toJson(this)
    }
}
