package id.bni46.microatmsdk.data

import androidx.annotation.Keep
import com.google.gson.Gson

@Keep
data class BluetoothClient(val name: String, val macAddress: String, val bonded: Boolean) {
    fun toJson(): String? {
        return Gson().toJson(this)
    }
}
