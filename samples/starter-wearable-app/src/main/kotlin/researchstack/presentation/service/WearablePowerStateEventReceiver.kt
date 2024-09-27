package researchstack.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import researchstack.domain.model.events.WearablePowerState
import researchstack.domain.repository.WearableEventRepository
import javax.inject.Inject

@AndroidEntryPoint
class WearablePowerStateEventReceiver : BroadcastReceiver() {
    @Inject
    lateinit var wearablePowerStateEventRepository: WearableEventRepository<WearablePowerState>
    override fun onReceive(context: Context, intent: Intent?) {
        intent?.apply {
            if (action == Intent.ACTION_SHUTDOWN || action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
                wearablePowerStateEventRepository.insert(WearablePowerState(powerState = if (action == Intent.ACTION_SHUTDOWN) 0 else 1))
            }
        }
    }
}
