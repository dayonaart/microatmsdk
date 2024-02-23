package id.bni46.microatmsdk.utilities

import android.app.Activity
import android.content.Intent
import id.bni46.microatmsdk.constan.Constant
import io.flutter.plugin.common.PluginRegistry

interface ActivityResultListener : PluginRegistry.ActivityResultListener {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        when (requestCode) {
            Constant.REQUEST_BLUETOOTH_ON -> {
                if (resultCode == Activity.RESULT_OK) {
                    ScanDevicesEvent.startScan()
                } else {
                    return true
                }
                return true
            }

            Constant.CHECK_BALANCE_CODE -> {
                return true
            }
        }
        return false
    }
}