package id.bni46.microatmsdk.devices.testing

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import id.bni46.microatmsdk.data.EmvResult
import id.bni46.microatmsdk.data.MethodResult
import id.bni46.microatmsdk.data.ProgressMessage
import id.bni46.microatmsdk.utilities.MessageProgressEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface MyDevice {
    val activity: Activity?

    suspend fun connect(device: BluetoothDevice): Boolean {
        return withContext(Dispatchers.IO) {
            delay(500)
            MessageProgressEvent.sentDataOnMainThread(
                ProgressMessage(
                    message = "Connecting ${device.name}",
                    hasProgress = true
                )
            )
            delay(1000)
            return@withContext true
        }
    }

    suspend fun startEmv(): String? {
        return withContext(Dispatchers.Main) {
            MessageProgressEvent.sentData(
                ProgressMessage(message = "Please wait")
            )
            delay(1000)
            MessageProgressEvent.sentData(
                ProgressMessage(message = "Reading Card")
            )
            delay(1000)
            MessageProgressEvent.sentData(
                ProgressMessage(message = "Input Your Password")
            )
            delay(1000)
            MessageProgressEvent.sentData(
                ProgressMessage(message = "Finishing")
            )
            delay(1000)
            MessageProgressEvent.sentData(
                ProgressMessage(message = "Stopping")
            )
            return@withContext EmvResult(
                communication = "communication",
                t2d = "t2d".repeat(10),
                icCardData = "ic".repeat(40),
                expDate = "080898",
                cardType = "cardType",
                pan = "1".repeat(16)
            ).toJson()
        }
    }

    fun injectMasterKey(): String? {
        return MethodResult(message = "SUCCESS INJECT MASTER KEY", data = true).toJson()
    }

    fun injectWorkKey(): String? {
        return MethodResult(message = "SUCCESS WORK KEY", data = true).toJson()
    }

    fun inputtingPin(): String? {
        return MethodResult(message = "SUCCESS INPUT PIN", data = "ABASD".repeat(4)).toJson()
    }

    fun encryption(data: String, context: Context): String? {
        return MethodResult(message = "SUCCESS ENCRYPTION", data = data).toJson()
    }

    fun decryption(data: String, context: Context): String? {
        return MethodResult(message = "SUCCESS DECRYPTION", data = data).toJson()
    }
}