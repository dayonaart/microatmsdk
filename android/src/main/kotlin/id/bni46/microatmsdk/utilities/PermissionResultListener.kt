package id.bni46.microatmsdk.utilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import id.bni46.microatmsdk.constan.Constant
import id.bni46.microatmsdk.data.MethodResult
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

interface PermissionResultListener : PluginRegistry.RequestPermissionsResultListener {
    val result: MethodChannel.Result?
    val activity: Activity
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        when (requestCode) {
            Constant.PERMISSION_CODE -> {
                val check = grantResults.isNotEmpty() &&
                        grantResults.all { it == 0 }
                if (check) {
                    result?.success(
                        MethodResult(
                            message = "Permission granted",
                            data = true
                        ).toJson()
                    )
                } else {
                    neverAskAgain()
                    result?.success(
                        MethodResult(
                            message = "Permission Enable Required",
                            data = false
                        ).toJson()
                    )
                }
                return true
            }
        }
        return false
    }

    private fun neverAskAgain() {
        val s = Constant.REQUEST_PERMISSION.map {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }.filter { it }
        if (s.all { false }) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }
    }
}