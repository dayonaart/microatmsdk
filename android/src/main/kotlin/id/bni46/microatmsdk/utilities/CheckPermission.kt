package id.bni46.microatmsdk.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import id.bni46.microatmsdk.constan.Constant
import id.bni46.microatmsdk.data.MethodResult
import io.flutter.plugin.common.MethodChannel

@SuppressLint("StaticFieldLeak")
object CheckPermission : PermissionResultListener {
    override var result: MethodChannel.Result? = null
    override lateinit var activity: Activity
    fun init(activity: Activity?, result: MethodChannel.Result): Boolean {
        CheckPermission.result = result
        if (activity != null) {
            CheckPermission.activity = activity
        }
        return activity != null
    }

    fun checkPermission() {
        val check =
            Constant.REQUEST_PERMISSION.all {
                val permission =
                    ContextCompat.checkSelfPermission(activity, it) ==
                            PackageManager.PERMISSION_GRANTED
                permission
            }
        if (check) {
            result?.success(MethodResult(message = "Permission granted", data = true).toJson())
        } else {
            ActivityCompat.requestPermissions(
                activity,
                Constant.REQUEST_PERMISSION,
                Constant.PERMISSION_CODE
            )
        }
    }
}