package id.bni46.microatmsdk

import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Context
import id.bni46.microatmsdk.extension.EventName
import id.bni46.microatmsdk.utilities.CheckPermission
import id.bni46.microatmsdk.utilities.MessageProgressEvent
import id.bni46.microatmsdk.utilities.MicroMethod
import id.bni46.microatmsdk.utilities.ScanDevicesEvent
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MicroPlugin : FlutterPlugin, MicroMethod, ActivityAware {
    private val TAG = "MicroPlugin"
    private lateinit var channel: MethodChannel
    private lateinit var scanDeviceListener: EventChannel
    private lateinit var progressMessageListener: EventChannel
    override var activity: Activity? = null
    override lateinit var context: Context
    override lateinit var bluetoothManager: BluetoothManager
    override fun onAttachedToEngine(
        flutterPluginBinding: FlutterPlugin.FlutterPluginBinding
    ) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "microatmsdk")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
        scanDeviceListener =
            EventChannel(flutterPluginBinding.binaryMessenger, EventName.SCAN_DEVICES_LISTENER.name)
        scanDeviceListener.setStreamHandler(ScanDevicesEvent)
        progressMessageListener =
            EventChannel(
                flutterPluginBinding.binaryMessenger,
                EventName.PROGRESS_MESSAGE_LISTENER.name
            )
        progressMessageListener.setStreamHandler(MessageProgressEvent)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addRequestPermissionsResultListener(CheckPermission)
        binding.addActivityResultListener(ScanDevicesEvent)
        bluetoothManager =
            (binding.activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)

    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null

    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addRequestPermissionsResultListener(CheckPermission)
        binding.addActivityResultListener(ScanDevicesEvent)
        bluetoothManager =
            (binding.activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

}
