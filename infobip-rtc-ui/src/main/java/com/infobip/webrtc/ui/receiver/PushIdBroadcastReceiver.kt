package com.infobip.webrtc.ui.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.infobip.webrtc.CallRegistrationWorker
import com.infobip.webrtc.Injector
import com.infobip.webrtc.ui.model.RtcUiMode
import org.infobip.mobile.messaging.BroadcastParameter.EXTRA_INFOBIP_ID

class PushIdBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (Injector.cache.rtcUiMode == RtcUiMode.DEFAULT) {
            intent?.getStringExtra(EXTRA_INFOBIP_ID)?.let { pushRegId ->
                CallRegistrationWorker.launch(context, pushRegId)
            }
        }
    }
}