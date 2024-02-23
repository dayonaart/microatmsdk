package id.bni46.microatmsdk.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.google.gson.Gson
import id.bni46.microatmsdk.constan.Constant
import id.bni46.microatmsdk.data.BluetoothClient
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink

@SuppressLint("StaticFieldLeak")
object ScanDevicesEvent : ActivityResultListener, EventChannel.StreamHandler {
    private const val TAG = "ScanDevices"
    private lateinit var activity: Activity
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private var sink: EventSink? = null

    fun init(activity: Activity?, bluetoothManager: BluetoothManager): Boolean {
        try {
            if (activity == null) return false
            this.activity = activity
            this.bluetoothManager = bluetoothManager
            bluetoothAdapter = bluetoothManager.adapter
            return true
        } catch (e: Exception) {
            Log.d(TAG, "initialize: ${e.message}")
            return false
        }
    }

    fun startScan() {
        if (!bluetoothManager.adapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, Constant.REQUEST_BLUETOOTH_ON)
            return
        }
        if (bluetoothAdapter.startDiscovery()) {
            val btDiscoverIntent =
                IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED).apply {
                    addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                    addAction(BluetoothDevice.ACTION_FOUND)
                }
            activity.registerReceiver(MicroReceiver, btDiscoverIntent)
        }
    }

    fun sentData(deviceList: List<BluetoothClient>) {
        sink?.success(Gson().toJson(deviceList))
    }

    override fun onListen(arguments: Any?, events: EventSink?) {
        sink = events
    }

    override fun onCancel(arguments: Any?) {
        sink = null
    }
}