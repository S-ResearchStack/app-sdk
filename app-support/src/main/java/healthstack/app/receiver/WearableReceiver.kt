package healthstack.app.receiver

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.ChannelClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import healthstack.common.HEALTH_DATA_FOLDER_NAME
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

abstract class WearableReceiver : WearableListenerService() {
    override fun onChannelOpened(channel: ChannelClient.Channel) {
        super.onChannelOpened(channel)
        Log.i(TAG, "channel opened ${channel.path}")

        val channelClient = Wearable.getChannelClient(baseContext)
        if (channel.path.contains(".csv")) {
            val outputDir = "${application.filesDir}" + HEALTH_DATA_FOLDER_NAME
            Files.createDirectories(Paths.get(outputDir))
            val newFile = File(outputDir, channel.path)

            val channelCallback = object : ChannelClient.ChannelCallback() {
                override fun onInputClosed(
                    channel: ChannelClient.Channel,
                    closeReason: Int,
                    errorCode: Int,
                ) {
                    super.onInputClosed(channel, closeReason, errorCode)
                    if (closeReason != CLOSE_REASON_NORMAL) {
                        Log.e(TAG, "wear->mobile ${channel.path} fail code: $closeReason")
                        newFile.delete()
                    } else {
                        Log.i(TAG, "wear->mobile ${channel.path} success")
                    }

                    channelClient.close(channel)
                    channelClient.unregisterChannelCallback(this)
                }
            }

            kotlin.runCatching {
                Tasks.await(channelClient.registerChannelCallback(channel, channelCallback))
                Tasks.await(channelClient.receiveFile(channel, Uri.fromFile(newFile), false))
            }.onFailure {
                Log.e(TAG, "failed to receive file: ${it.message}")
                channelClient.unregisterChannelCallback(channel, channelCallback)
            }
        } else {
            Log.e(TAG, "not supported channel path: ${channel.path}")
            channelClient.close(channel)
        }
    }

    companion object {
        private val TAG = WearableReceiver::class.simpleName
    }
}
