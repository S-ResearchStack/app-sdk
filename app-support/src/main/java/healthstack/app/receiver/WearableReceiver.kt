package healthstack.app.receiver

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.ChannelClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import healthstack.common.room.WearableAppDataBase
import healthstack.common.model.EcgSet

abstract class WearableReceiver : WearableListenerService() {

    private val gson = Gson()
    override fun onChannelOpened(channel: ChannelClient.Channel) {
        super.onChannelOpened(channel)

        val channelClient = Wearable.getChannelClient(baseContext)
        kotlin.runCatching {
            Tasks.await(channelClient.getInputStream(channel))
                .use {
                    String(it.readBytes())
                }
        }.onSuccess {
            WearableAppDataBase.getInstance()?.ecgDao()?.insertAll(
                gson.fromJson(it, object : TypeToken<ArrayList<EcgSet>>() {}.type)
            )
            channelClient.close(channel)
        }.onFailure {
            Log.e(TAG, "${it.message}")
        }
    }

    companion object {
        private val TAG = WearableReceiver::class.simpleName
    }
}
