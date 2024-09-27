package researchstack.data.datasource.mapper

import org.junit.Ignore
import org.junit.jupiter.api.Test
import researchstack.data.datasource.grpc.mapper.toProto
import researchstack.data.datasource.local.room.entity.Speed
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.SpO2
import researchstack.domain.model.priv.SweatLoss
import researchstack.domain.model.sensor.Light

class HealthDataGrpcMapperTest {
    @Ignore
    fun `test priv data to proto`() {
        listOf(
            SpO2().toDataMap(),
            EcgSet(ppgGreens = listOf(PpgGreen(), PpgGreen())).toDataMap(),
            PpgRed().toDataMap(),
            PpgGreen().toDataMap(),
            PpgIr().toDataMap(),
            Accelerometer().toDataMap(),
            HeartRate().toDataMap(),
            SweatLoss().toDataMap(),
            Bia().toDataMap()
        )
            .forEach {
                it.toProto()
            }
    }

    @Test
    fun `test another data to proto`() {
        listOf(
            Light(10, 20, 1000).toDataMap(),
            researchstack.domain.model.sensor.Accelerometer(
                20f,
                50f,
                80f,
                1000
            ).toDataMap(),
            Speed(10f, 1, 10, 1000).toDataMap()
        )
            .forEach {
                it.toProto()
            }
    }
}
