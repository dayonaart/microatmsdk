package id.bni46.microatmsdk.utilities

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import id.bni46.microatmsdk.data.BluetoothClient
import id.bni46.microatmsdk.data.MethodResult
import id.bni46.microatmsdk.devices.testing.MyDevice
import id.bni46.microatmsdk.extension.MethodName
import id.bni46.microatmsdk.extension.methodName
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface MicroMethod : MethodCallHandler, MyDevice {
    override val activity: Activity?
    val context: Context
    var bluetoothManager: BluetoothManager

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.methodName()) {
            MethodName.CHECK_PERMISSION -> {
                if (CheckPermission.init(activity, result)) {
                    CheckPermission.checkPermission()
                } else {
                    result.success(
                        MethodResult(message = "Activity not found", data = null).toJson()
                    )
                }
            }

            MethodName.SCAN_DEVICES -> {
                if (ScanDevicesEvent.init(activity, bluetoothManager)) {
                    ScanDevicesEvent.startScan()
                    result.success(MethodResult(message = "SCANNING", data = true).toJson())
                } else {
                    result.success(
                        MethodResult(message = "Activity not found", data = false).toJson()
                    )
                }
            }

            MethodName.CANCEL_SCANNING -> {
                if (bluetoothManager.adapter.cancelDiscovery()) {
                    result.success(
                        MethodResult(
                            message = "Cancel Scanning Success",
                            data = true
                        ).toJson()
                    )
                } else {
                    result.success(
                        MethodResult(
                            message = "Cancel Scanning Failed",
                            data = false
                        ).toJson()
                    )
                }
            }

            MethodName.CONNECTING -> {
                try {
                    val macAddress = call.arguments as String
                    val device = bluetoothManager.adapter.getRemoteDevice(macAddress)
                    bluetoothManager.adapter.cancelDiscovery()
                    CoroutineScope(Dispatchers.IO).launch {
                        if (connect(device)) {
                            result.success(
                                MethodResult(
                                    message = "Successfully Connected with ${device.name}",
                                    data = BluetoothClient(
                                        name = device.name,
                                        macAddress = device.address,
                                        bonded = device.bondState == BluetoothDevice.BOND_BONDED
                                    ).toJson()
                                ).toJson()
                            )
                        } else {
                            result.success(
                                MethodResult(
                                    message = "Failed Connect to ${device.name}",
                                    data = null
                                ).toJson()
                            )
                        }
                    }
                } catch (e: Exception) {
                    result.success(
                        MethodResult(
                            message = "${e.message}",
                            data = null
                        ).toJson()
                    )
                }
            }

            MethodName.PAIRING -> {
                try {
                    val macAddress = call.arguments as String
                    val device = bluetoothManager.adapter.getRemoteDevice(macAddress)
                    bluetoothManager.adapter.cancelDiscovery()
                    CoroutineScope(Dispatchers.IO).launch {
                        Pairing.start(activity, device)
                        result.success(
                            MethodResult(
                                message = "PAIRING",
                                data = "Pair With ${device.name}"
                            ).toJson()
                        )
                    }
                } catch (e: Exception) {
                    result.success(
                        MethodResult(
                            message = "${e.message}",
                            data = null
                        ).toJson()
                    )
                }
            }

            MethodName.START_EMV -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val emv = startEmv()
                    result.success(emv)
                }
            }

            MethodName.INJECT_MASTER_KEY -> {
                result.success(injectMasterKey())
            }

            MethodName.INJECT_WORK_KEY -> {
                result.success(injectWorkKey())
            }

            MethodName.ENCRYPT_DATA -> {
                val data = call.arguments as String
                result.success(encryption(data, context))
            }

            MethodName.DECRYPT_DATA -> {
                val data = call.arguments as String
                result.success(decryption(data, context))
            }

            MethodName.NOT_IMPLEMENTED -> result.notImplemented()

        }
    }
}