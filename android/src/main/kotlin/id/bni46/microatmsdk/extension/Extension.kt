package id.bni46.microatmsdk.extension

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import com.mf.mpos.pub.CommEnum
import id.bni46.microatmsdk.BuildConfig
import id.bni46.microatmsdk.constan.BytesUtil
import id.bni46.microatmsdk.constan.SecurityUtil
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

fun MethodChannel.Result.sent(success: Boolean) {
    this.success(success)
}

enum class MethodName { CHECK_PERMISSION, SCAN_DEVICES, CANCEL_SCANNING, CONNECTING, PAIRING, START_EMV, INJECT_MASTER_KEY, INJECT_WORK_KEY, ENCRYPT_DATA, DECRYPT_DATA, NOT_IMPLEMENTED }
enum class EventName { SCAN_DEVICES_LISTENER, PROGRESS_MESSAGE_LISTENER }

fun MethodCall.methodName(): MethodName {
    return MethodName.valueOf(this.method)
}

@SuppressLint("MissingPermission")
fun Intent.blueDeviceData(useFilter: Boolean): BluetoothDevice? {
    try {
        val pattern = "\\w{2}-\\d{8}"
        val devices =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                this.getParcelableExtra(
                    BluetoothDevice.EXTRA_DEVICE,
                    BluetoothDevice::class.java
                )
            } else {
                this.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            }
        if (!useFilter) {
            return devices
        }
        return if (Regex(pattern).matches(devices!!.name)) {
            devices
        } else {
            null
        }
    } catch (e: Exception) {
        return null
    }
}


fun String.calcKvc(masterKey: Boolean): String {
    val data = "0".repeat(16)
    return if (masterKey) {
        val kvc = SecurityUtil.doubleDes(
            BytesUtil.hexString2ByteArray(this),
            BytesUtil.hexString2ByteArray(data)
        )
        BytesUtil.bytes2Hex(kvc).substring(0, 8)
    } else {
        val mkPlain = SecurityUtil.doubleUnDes(
            BytesUtil.hexString2ByteArray(BuildConfig.MASTER_KEY),
            BytesUtil.hexString2ByteArray(this)
        )
        val kvc = SecurityUtil.doubleDes(mkPlain, BytesUtil.hexString2ByteArray(data))
        return BytesUtil.bytes2Hex(kvc).substring(0, 8)
    }

}

fun String.keyIndex(): CommEnum.KEYINDEX {
    when (this) {
        BuildConfig.MASTER_KEY -> {
            return CommEnum.KEYINDEX.INDEX0
        }

        BuildConfig.PIN_KEY -> {
            return CommEnum.KEYINDEX.INDEX1
        }

        BuildConfig.TDK_KEY -> {
            return CommEnum.KEYINDEX.INDEX2
        }

        BuildConfig.MAC_KEY -> {
            return CommEnum.KEYINDEX.INDEX3
        }

        else -> {
            return CommEnum.KEYINDEX.INDEX0
        }
    }
}

fun String.toHex(): String {
    val str = StringBuilder()
    for (ch in this.toCharArray()) {
        str.append(String.format("%02x", ch.code))
    }
    return str.toString()
}