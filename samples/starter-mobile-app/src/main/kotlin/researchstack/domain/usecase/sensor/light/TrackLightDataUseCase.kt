package researchstack.domain.usecase.sensor.light

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import researchstack.domain.model.sensor.Light
import researchstack.domain.repository.sensor.LightRepository
import researchstack.util.toEpochMilli
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class TrackLightDataUseCase @Inject constructor(private val lightRepository: LightRepository) {
    operator fun invoke(): Flow<Light> = callbackFlow {
        var light: Light? = null

        val timer = fixedRateTimer(period = LIGHT_RESOLUTION) {
            light?.let {
                trySend(
                    Light(it.accuracy, it.lx, LocalDateTime.now().toEpochMilli())
                )
            }
        }

        lightRepository.startTracking().collect { light = it }
        timer.cancel()
        channel.close()
    }

    companion object {
        private const val LIGHT_RESOLUTION = 1000L
    }
}
