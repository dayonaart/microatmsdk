package id.bni46.microatmsdk.data

import androidx.annotation.Keep
import com.google.gson.Gson

@Keep
data class EmvResult(
    val communication: String,
    val cardType: String,
    val pan: String,
    val t2d: String,
    val icCardData: String,
    val expDate: String
) {
    fun toJson(): String? {
        return Gson().toJson(this)
    }
}