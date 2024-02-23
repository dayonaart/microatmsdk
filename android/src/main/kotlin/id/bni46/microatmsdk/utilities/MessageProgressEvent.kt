package id.bni46.microatmsdk.utilities

import com.google.gson.Gson
import id.bni46.microatmsdk.data.ProgressMessage
import io.flutter.plugin.common.EventChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MessageProgressEvent : EventChannel.StreamHandler {
    private var sink: EventChannel.EventSink? = null

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        sink = events
        events?.success(ProgressMessage(message = "Searching", hasProgress = true).toJson())
    }

    override fun onCancel(arguments: Any?) {
        sink = null
    }

    fun sentData(message: ProgressMessage) {
        sink?.success(Gson().toJson(message))
    }

    fun sentDataOnMainThread(message: ProgressMessage) {
        CoroutineScope(Dispatchers.Main).launch {
            sink?.success(Gson().toJson(message))
        }
    }
}