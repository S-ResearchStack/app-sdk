package researchstack.wearable.standalone.domain.usecase

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.flow.first
import org.apache.commons.io.input.ReaderInputStream
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.local.pref.PrivDataOnOffPref
import researchstack.data.local.pref.PrivDataOnOffPref.Companion.PERMITTED_DATA_PREF_KEY
import researchstack.domain.model.TimestampMapData
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.priv.SpO2
import researchstack.domain.model.priv.SweatLoss
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.WearableDataRepository
import researchstack.wearable.standalone.BuildConfig
import researchstack.wearable.standalone.data.local.pref.dataStore
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

// FIXME should refactor
@Suppress("LongParameterList")
class SyncPrivDataUseCase @Inject constructor(
    private val accMeterFileRepository: WearableDataRepository<Accelerometer>,
    private val biaFileRepository: WearableDataRepository<Bia>,
    private val ecgFileRepository: WearableDataRepository<EcgSet>,
    private val ppgGreenFileRepository: WearableDataRepository<PpgGreen>,
    private val ppgIrFileRepository: WearableDataRepository<PpgIr>,
    private val ppgRedFileRepository: WearableDataRepository<PpgRed>,
    private val spO2FileRepository: WearableDataRepository<SpO2>,
    private val sweatLossFileRepository: WearableDataRepository<SweatLoss>,
    private val heartRateFileRepository: WearableDataRepository<HeartRate>,
    private val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
) {
    private val objectMapper = jacksonObjectMapper().apply {
        class ListDeserializer : JsonDeserializer<List<*>>(), ContextualDeserializer {
            private val objectMapper = ObjectMapper()
            private val types: MutableMap<String, JavaType> = mutableMapOf()

            override fun createContextual(
                ctxt: DeserializationContext,
                property: BeanProperty
            ): JsonDeserializer<List<*>> {
                types[property.name] = property.type
                return this
            }

            @Throws(IOException::class)
            override fun deserialize(
                jsonParser: JsonParser,
                deserializationContext: DeserializationContext
            ): List<*> {
                val node: JsonNode = jsonParser.codec.readTree(jsonParser)
                val jsonStr = node.toString().drop(1).dropLast(1).replace("\\\"", "\"")
                return objectMapper.readValue(jsonStr, types[jsonParser.parsingContext.currentName])
            }
        }

        val listModule = SimpleModule().addDeserializer(List::class.java, ListDeserializer())
        registerModule(listModule)
        registerModule(JavaTimeModule())
    }

    suspend operator fun invoke(context: Context) = runCatching {
        PrivDataOnOffPref(context.dataStore, PERMITTED_DATA_PREF_KEY).privDataTypesFlow.first().map {
            when (it) {
                PrivDataType.WEAR_ACCELEROMETER -> it.syncFile(accMeterFileRepository)
                PrivDataType.WEAR_BIA -> it.syncFile(biaFileRepository)
                PrivDataType.WEAR_ECG -> it.syncFile(ecgFileRepository)
                PrivDataType.WEAR_PPG_GREEN -> it.syncFile(ppgGreenFileRepository)
                PrivDataType.WEAR_PPG_IR -> it.syncFile(ppgIrFileRepository)
                PrivDataType.WEAR_PPG_RED -> it.syncFile(ppgRedFileRepository)
                PrivDataType.WEAR_SPO2 -> it.syncFile(spO2FileRepository)
                PrivDataType.WEAR_SWEAT_LOSS -> it.syncFile(sweatLossFileRepository)
                PrivDataType.WEAR_HEART_RATE -> it.syncFile(heartRateFileRepository)
            }
        }.find { it.isFailure }?.getOrThrow()
    }

    private suspend fun PrivDataType.syncFile(
        wearableDataRepository: WearableDataRepository<*>,
    ) = runCatching {
        wearableDataRepository.getCompletedFiles().forEach { file ->
            FileInputStream(file).use {
                val reader = BufferedReader(InputStreamReader(it))
                val dataType = PrivDataType.valueOf(reader.readLine())

                parseFile(dataType, ReaderInputStream(reader))
                    .onSuccess {
                        Log.i(TAG, "succeeded to sync ${file.name}, so delete it")
                        file.delete()
                    }
                    .onFailure {
                        Log.e(TAG, "failed to sync ${file.name}")
                    }
            }
        }
    }

    private suspend fun parseFile(dataType: PrivDataType, csvInputStream: InputStream): Result<Unit> = runCatching {
        val data = when (dataType) {
            PrivDataType.WEAR_ACCELEROMETER -> readCsv<Accelerometer>(csvInputStream)
            PrivDataType.WEAR_BIA -> readCsv<Bia>(csvInputStream)
            PrivDataType.WEAR_ECG -> readCsv<EcgSet>(csvInputStream)
            PrivDataType.WEAR_PPG_GREEN -> readCsv<PpgGreen>(csvInputStream)
            PrivDataType.WEAR_PPG_IR -> readCsv<PpgIr>(csvInputStream)
            PrivDataType.WEAR_PPG_RED -> readCsv<PpgRed>(csvInputStream)
            PrivDataType.WEAR_SPO2 -> readCsv<SpO2>(csvInputStream)
            PrivDataType.WEAR_SWEAT_LOSS -> readCsv<SweatLoss>(csvInputStream)
            PrivDataType.WEAR_HEART_RATE -> readCsv<HeartRate>(csvInputStream)
        }

        grpcHealthDataSynchronizer.syncHealthData(
            listOf(BuildConfig.STUDY_ID),
            toHealthDataModel(dataType, data)
        ).onSuccess {
            Log.i(TAG, "success to upload data: $dataType")
        }.onFailure {
            Log.e(TAG, "fail to upload data to server")
            Log.e(TAG, it.stackTraceToString())
        }.getOrThrow()
    }

    private inline fun <reified T> readCsv(inputStream: InputStream): List<T> {
        val csvMapper = CsvMapper().apply {
            enable(CsvParser.Feature.TRIM_SPACES)
            enable(CsvParser.Feature.SKIP_EMPTY_LINES)
        }

        val schema = CsvSchema.emptySchema().withHeader().withColumnSeparator('|')

        val data = csvMapper.readerFor(Map::class.java)
            .with(schema)
            .readValues<Map<String, String>>(inputStream)
            .readAll()
            .map {
                objectMapper.convertValue(it, T::class.java)
            }

        Log.i(TAG, "data synced from wearOS: ${T::class.java.simpleName}, size: ${data.size}")

        return data
    }

    private fun <T : TimestampMapData> toHealthDataModel(dataType: PrivDataType, data: List<T>): HealthDataModel {
        return HealthDataModel(dataType, data.map { it.toDataMap() })
    }

    companion object {
        private val TAG = SyncPrivDataUseCase::class.simpleName
    }
}
