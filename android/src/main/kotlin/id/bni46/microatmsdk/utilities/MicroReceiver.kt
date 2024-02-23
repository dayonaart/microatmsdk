package id.bni46.microatmsdk.utilities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import id.bni46.microatmsdk.data.BluetoothClient
import id.bni46.microatmsdk.data.ProgressMessage
import id.bni46.microatmsdk.extension.blueDeviceData

object MicroReceiver : BroadcastReceiver() {
    private const val TAG = "MicroReceiver"
    private var deviceList = listOf<BluetoothClient>()

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                deviceList = listOf()
                Log.d(TAG, "ACTION_DISCOVERY_STARTED")
            }

            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                Log.d(TAG, "ACTION_DISCOVERY_FINISHED")
                if (deviceList.isEmpty()) {
                    MessageProgressEvent.sentData(
                        ProgressMessage(message = "No Device Found", hasProgress = false)
                    )
                } else {
                    MessageProgressEvent.sentData(
                        ProgressMessage(message = "Micro Atm Devices", hasProgress = false)
                    )
                }
            }

            BluetoothDevice.ACTION_FOUND -> {
                Log.d(TAG, "ACTION_FOUND")
                val device = intent.blueDeviceData(false)
                updateDeviceList(device)
                ScanDevicesEvent.sentData(
                    deviceList.sortedBy { it.bonded }.toSet().reversed().toList()
                )
            }

            BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                val reason = intent.extras?.getInt("android.bluetooth.device.extra.REASON")
                val device = intent.blueDeviceData(false)
                updateDeviceList(device)
                if (device?.bondState == BluetoothDevice.BOND_BONDED) {
                    ScanDevicesEvent.sentData(deviceList.filter { it.bonded }
                        .toSet().toList())
                    MessageProgressEvent.sentData(
                        ProgressMessage(message = "Micro Atm Devices", hasProgress = false)
                    )
                } else if (reason != 0) {
                    ScanDevicesEvent.sentData(deviceList.sortedBy { it.bonded }
                        .toSet().reversed().toList())
                    MessageProgressEvent.sentData(
                        ProgressMessage(
                            message = "Error Pairing with ${device?.name}",
                            hasProgress = false
                        )
                    )
                } else {
                    ScanDevicesEvent.sentData(deviceList.filter { it.macAddress == device?.address }
                        .toSet().toList())
                }
            }
        }
    }

    private fun updateDeviceList(device: BluetoothDevice?) {
        deviceList += BluetoothClient(
            name = device?.name ?: "",
            macAddress = device?.address ?: "",
            bonded = device?.bondState == BluetoothDevice.BOND_BONDED
        )
    }
}