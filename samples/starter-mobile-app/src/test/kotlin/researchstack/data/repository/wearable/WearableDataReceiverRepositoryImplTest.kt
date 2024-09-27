package researchstack.data.repository.wearable

import io.mockk.mockk
import org.apache.commons.io.input.ReaderInputStream
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import researchstack.POSITIVE_TEST
import researchstack.data.datasource.grpc.GrpcHealthDataSynchronizerImpl
import researchstack.data.local.room.WearableAppDataBase
import researchstack.domain.model.TimestampMapData
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.Ecg
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.priv.SpO2
import researchstack.domain.model.priv.SweatLoss
import researchstack.domain.repository.ShareAgreementRepository
import researchstack.domain.repository.StudyRepository
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Stream

internal class WearableDataReceiverRepositoryImplTest {
    private val wearableRepo = WearableDataReceiverRepositoryImpl(
        mockk<StudyRepository>(),
        mockk<ShareAgreementRepository>(),
        mockk<WearableAppDataBase>(),
        mockk<GrpcHealthDataSynchronizerImpl>(),
    )

    @Tag(POSITIVE_TEST)
    @ParameterizedTest
    @MethodSource("provideParameters")
    fun `readCsv should work properly`(
        csvInputStream: InputStream,
        expectedDataType: PrivDataType,
        expectedResult: List<TimestampMapData>,
    ) {
        csvInputStream
            .use {
                val reader = BufferedReader(InputStreamReader(it))
                val dataType = PrivDataType.valueOf(reader.readLine())
                val result = when (dataType) {
                    PrivDataType.WEAR_ACCELEROMETER -> wearableRepo.readCsv<Accelerometer>(ReaderInputStream(reader))
                    PrivDataType.WEAR_BIA -> wearableRepo.readCsv<Bia>(ReaderInputStream(reader))
                    PrivDataType.WEAR_ECG -> wearableRepo.readCsv<EcgSet>(ReaderInputStream(reader))
                    PrivDataType.WEAR_PPG_GREEN -> wearableRepo.readCsv<PpgGreen>(ReaderInputStream(reader))
                    PrivDataType.WEAR_PPG_IR -> wearableRepo.readCsv<PpgIr>(ReaderInputStream(reader))
                    PrivDataType.WEAR_PPG_RED -> wearableRepo.readCsv<PpgRed>(ReaderInputStream(reader))
                    PrivDataType.WEAR_SPO2 -> wearableRepo.readCsv<SpO2>(ReaderInputStream(reader))
                    PrivDataType.WEAR_SWEAT_LOSS -> wearableRepo.readCsv<SweatLoss>(ReaderInputStream(reader))
                    PrivDataType.WEAR_HEART_RATE -> wearableRepo.readCsv<HeartRate>(ReaderInputStream(reader))
                }

                assertEquals(expectedDataType, dataType)
                assertEquals(expectedResult, result)
            }
    }

    companion object {
        @JvmStatic
        private fun provideParameters(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    """
                        WEAR_ACCELEROMETER
                        timeOffset|x|y|z|timestamp
                        0|1|2|3|1234567890123456
                        0|-54|7980|0|-1234567890123456
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_ACCELEROMETER,
                    listOf(
                        Accelerometer(1234567890123456, 1, 2, 3, 0),
                        Accelerometer(-1234567890123456, -54, 7980, 0, 0),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_BIA
                        basalMetabolicRate|bodyFatMass|bodyFatRatio|fatFreeMass|fatFreeRatio|skeletalMuscleMass|skeletalMuscleRatio|totalBodyWater|measurementProgress|status|timestamp
                        1|2|3|4|5|6|7|8|9|10|1234567890123456
                        .1|.2|.3|.4|.5|.6|.7|.8|.9|-10|-1234567890123456
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_BIA,
                    listOf(
                        Bia(1234567890123456, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10),
                        Bia(-1234567890123456, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, -10),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_ECG
                        ecgs|leadOff|maxThreshold|minThreshold|ppgGreens|sequence
                        [{"timestamp": 1234567890123456, "ecg": -70}]|1|2|3|[]|4
                        [{"timestamp": 1, "ecg": 2}, {"timestamp": 3, "ecg": 4}]|100|84|3|[{"timestamp": 1, "ppg": 2}, {"timestamp": 3, "ppg": 4}]|2
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_ECG,
                    listOf(
                        EcgSet(listOf(Ecg(1234567890123456, -70f)), listOf(), 1, 2f, 3f, 4),
                        EcgSet(
                            listOf(Ecg(1, 2f), Ecg(3, 4f)),
                            listOf(PpgGreen(1, 2), PpgGreen(3, 4)),
                            100,
                            84f,
                            3f,
                            2,
                        ),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_PPG_GREEN
                        ppg|timestamp
                        0|1234567890123456
                        77|-1234567890123456
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_PPG_GREEN,
                    listOf(
                        PpgGreen(1234567890123456, 0),
                        PpgGreen(-1234567890123456, 77),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_PPG_IR
                        ppg|timestamp
                        0|1234567890123456
                        77|-1234567890123456
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_PPG_IR,
                    listOf(
                        PpgIr(1234567890123456, 0),
                        PpgIr(-1234567890123456, 77),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_PPG_RED
                        ppg|timestamp
                        0|1234567890123456
                        77|-1234567890123456
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_PPG_RED,
                    listOf(
                        PpgRed(1234567890123456, 0),
                        PpgRed(-1234567890123456, 77),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_SPO2
                        heartRate|spO2|status|timestamp
                        60|40|LOW_SIGNAL|1234567890123456
                        79|54|"DEVICE_MOVING"|-1234567890123456
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_SPO2,
                    listOf(
                        SpO2(1234567890123456, 60, 40, SpO2.Flag.LOW_SIGNAL),
                        SpO2(-1234567890123456, 79, 54, SpO2.Flag.DEVICE_MOVING),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_SWEAT_LOSS
                        sweatLoss|status|timestamp
                        0|0|1234567890123456
                        -3.14|123|-1234567890123456
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_SWEAT_LOSS,
                    listOf(
                        SweatLoss(1234567890123456, 0f, 0),
                        SweatLoss(-1234567890123456, -3.14f, 123),
                    )
                ),
                Arguments.of(
                    """
                        WEAR_HEART_RATE
                        heartRateStatus|ibiList|ibiStatusList|timestamp|value
                        88|[123,456,777]|[123,456,777]|1234567890123456|0
                        72|[745,138,745]|[371,462,513]|-1234567890123456|128
                    """.trimIndent().byteInputStream(),
                    PrivDataType.WEAR_HEART_RATE,
                    listOf(
                        HeartRate(1234567890123456, 0, listOf(123, 456, 777), listOf(123, 456, 777), 88),
                        HeartRate(-1234567890123456, 128, listOf(745, 138, 745), listOf(371, 462, 513), 72),
                    )
                ),
            )
        }
    }
}
