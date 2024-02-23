package id.bni46.microatmsdk.utilities

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import id.bni46.microatmsdk.data.ProgressMessage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

object Pairing {
    suspend fun start(activity: Activity?, device: BluetoothDevice) {
        coroutineScope {
            delay(500)
            MessageProgressEvent.sentDataOnMainThread(
                ProgressMessage(
                    message = "Pairing with ${device.name}",
                    hasProgress = true
                )
            )
            device.createBond()
            val bondIntent = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            activity?.registerReceiver(MicroReceiver, bondIntent)
        }
    }
}