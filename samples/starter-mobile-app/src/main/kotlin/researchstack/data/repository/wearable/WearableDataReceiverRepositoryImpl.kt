package researchstack.data.repository.wearable

import android.util.Log
import androidx.paging.PagingSource
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
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import researchstack.BuildConfig
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.local.room.WearableAppDataBase
import researchstack.data.local.room.dao.PrivDao
import researchstack.domain.model.Timestamp
import researchstack.domain.model.TimestampMapData
import researchstack.domain.model.log.DataSyncLog
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
import researchstack.domain.repository.ShareAgreementRepository
import researchstack.domain.repository.StudyRepository
import researchstack.domain.repository.WearableDataReceiverRepository
import researchstack.domain.usecase.log.AppLogger
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type
import javax.inject.Inject

class WearableDataReceiverRepositoryImpl @Inject constructor(
    private val studyRepository: StudyRepository,
    private val shareAgreementRepository: ShareAgreementRepository,
    private val wearableAppDataBase: WearableAppDataBase,
    private val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
) : WearableDataReceiverRepository {

    private val gson = Gson()

    val objectMapper = jacksonObjectMapper().apply {
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

    override fun saveWearableData(jsonObject: JsonObject) {
        val dataType = gson.fromJson(jsonObject.get("dataType"), PrivDataType::class.java)
        Log.i(TAG, "received datatype: $dataType")
        when (dataType) {
            PrivDataType.WEAR_ACCELEROMETER -> saveData<Accelerometer>(
                jsonObject,
                wearableAppDataBase.accelerometerDao()
            )

            PrivDataType.WEAR_BIA -> saveData<Bia>(jsonObject, wearableAppDataBase.biaDao())
            PrivDataType.WEAR_ECG -> saveData<EcgSet>(jsonObject, wearableAppDataBase.ecgDao())
            PrivDataType.WEAR_PPG_GREEN -> saveData<PpgGreen>(jsonObject, wearableAppDataBase.ppgGreenDao())
            PrivDataType.WEAR_PPG_IR -> saveData<PpgIr>(jsonObject, wearableAppDataBase.ppgIrDao())
            PrivDataType.WEAR_PPG_RED -> saveData<PpgRed>(jsonObject, wearableAppDataBase.ppgRedDao())
            PrivDataType.WEAR_SPO2 -> saveData<SpO2>(jsonObject, wearableAppDataBase.spO2Dao())
            PrivDataType.WEAR_SWEAT_LOSS -> saveData<SweatLoss>(jsonObject, wearableAppDataBase.sweatLossDao())
            PrivDataType.WEAR_HEART_RATE -> saveData<HeartRate>(jsonObject, wearableAppDataBase.heartRateDao())
        }
    }

    override fun saveWearableData(dataType: PrivDataType, csvInputStream: InputStream) {
        when (dataType) {
            PrivDataType.WEAR_ACCELEROMETER -> saveData<Accelerometer>(
                readCsv<Accelerometer>(csvInputStream),
                wearableAppDataBase.accelerometerDao()
            )

            PrivDataType.WEAR_BIA -> saveData<Bia>(readCsv<Bia>(csvInputStream), wearableAppDataBase.biaDao())
            PrivDataType.WEAR_ECG -> saveData<EcgSet>(readCsv<EcgSet>(csvInputStream), wearableAppDataBase.ecgDao())

            PrivDataType.WEAR_PPG_GREEN -> saveData<PpgGreen>(
                readCsv<PpgGreen>(csvInputStream),
                wearableAppDataBase.ppgGreenDao()
            )

            PrivDataType.WEAR_PPG_IR -> saveData<PpgIr>(readCsv<PpgIr>(csvInputStream), wearableAppDataBase.ppgIrDao())
            PrivDataType.WEAR_PPG_RED -> saveData<PpgRed>(
                readCsv<PpgRed>(csvInputStream),
                wearableAppDataBase.ppgRedDao()
            )

            PrivDataType.WEAR_SPO2 -> saveData<SpO2>(readCsv<SpO2>(csvInputStream), wearableAppDataBase.spO2Dao())
            PrivDataType.WEAR_SWEAT_LOSS -> saveData<SweatLoss>(
                readCsv<SweatLoss>(csvInputStream),
                wearableAppDataBase.sweatLossDao()
            )

            PrivDataType.WEAR_HEART_RATE -> saveData<HeartRate>(
                readCsv<HeartRate>(csvInputStream),
                wearableAppDataBase.heartRateDao()
            )
        }
    }

    inline fun <reified T> readCsv(inputStream: InputStream): List<T> {
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

        Log.i(
            WearableDataReceiverRepositoryImpl::class.simpleName,
            "data synced from wearOS: ${T::class.java.simpleName}, size: ${data.size}"
        )

        return data
    }

    private fun <T> fromJson(jsonObject: JsonObject, typeOfT: Type): T =
        gson.fromJson(
            jsonObject.get("data"),
            typeOfT
        )

    private fun <T> JsonObject.isArrayItem() =
        runCatching { fromJson<ArrayList<T>>(this, object : TypeToken<ArrayList<T>>() {}.type) }.getOrNull() != null

    private inline fun <reified T : Timestamp> saveData(jsonObject: JsonObject, privDao: PrivDao<T>) {
        if (jsonObject.isArrayItem<T>()) privDao.insertAll(
            fromJson(
                jsonObject,
                object : TypeToken<ArrayList<T>>() {}.type
            )
        )
        else privDao.insert(fromJson(jsonObject, object : TypeToken<T>() {}.type))
    }

    private inline fun <reified T : Timestamp> saveData(data: List<T>, privDao: PrivDao<T>) {
        privDao.insertAll(data)
    }

    override suspend fun syncWearableData() {
        studyRepository.getActiveStudies().first()
            .associate { (studyId) -> getAgreedWearableDataTypes(studyId).first() }
            .reverse()
            .forEach { (dataType, studyIds) ->
                syncRoomToServer(getDao(dataType), dataType, studyIds)
            }
    }

    override suspend fun syncWearableData(
        studyIds: List<String>,
        dataType: PrivDataType,
        csvInputStream: InputStream
    ) {
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
            studyIds,
            toHealthDataModel(dataType, data)
        ).onSuccess {
            Log.i(TAG, "success to upload data: $dataType")
            AppLogger.saveLog(DataSyncLog("sync $dataType ${data.size}"))
        }.onFailure {
            Log.e(TAG, "fail to upload data to server")
            Log.e(TAG, it.stackTraceToString())
            AppLogger.saveLog(DataSyncLog("FAIL: sync data $dataType ${it.stackTraceToString()}"))
        }.getOrThrow()
    }

    private fun getAgreedWearableDataTypes(studyId: String) =
        shareAgreementRepository.getAgreedWearableDataTypes(studyId)
            .map { dataTypes -> studyId to dataTypes }

    private fun <T1, T2> Map<T1, List<T2>>.reverse(): Map<T2, List<T1>> {
        val reversed = mutableMapOf<T2, MutableList<T1>>()

        forEach { (key, value) ->
            value.forEach {
                if (!reversed.contains(it)) reversed[it] = mutableListOf()
                reversed[it]?.add(key)
            }
        }

        return reversed
    }

    private fun getDao(dataType: PrivDataType) =
        when (dataType) {
            PrivDataType.WEAR_ACCELEROMETER -> wearableAppDataBase.accelerometerDao()
            PrivDataType.WEAR_BIA -> wearableAppDataBase.biaDao()
            PrivDataType.WEAR_ECG -> wearableAppDataBase.ecgDao()
            PrivDataType.WEAR_PPG_GREEN -> wearableAppDataBase.ppgGreenDao()
            PrivDataType.WEAR_PPG_IR -> wearableAppDataBase.ppgIrDao()
            PrivDataType.WEAR_PPG_RED -> wearableAppDataBase.ppgRedDao()
            PrivDataType.WEAR_SPO2 -> wearableAppDataBase.spO2Dao()
            PrivDataType.WEAR_SWEAT_LOSS -> wearableAppDataBase.sweatLossDao()
            PrivDataType.WEAR_HEART_RATE -> wearableAppDataBase.heartRateDao()
        }

    private suspend fun <T : TimestampMapData> syncRoomToServer(
        dao: PrivDao<T>,
        dataType: PrivDataType,
        studyIds: List<String>,
    ) {
        Log.i(TAG, "syncWearableData Start $dataType")
        var cnt = 0
        var check = false
        while (!check && cnt < 30) {
            Log.i(TAG, "current try: $cnt")
            cnt++
            check = true
            kotlin.runCatching {
                when (dataType) {
                    PrivDataType.WEAR_PPG_GREEN, PrivDataType.WEAR_ACCELEROMETER -> {
                        val batchHealthData = mutableListOf<List<T>>()
                        var lastTimestamp = -1L
                        val pageSource = dao.getGreaterThan(0)

                        var loadResult =
                            pageSource.load(
                                PagingSource.LoadParams.Refresh(
                                    null,
                                    BuildConfig.BATCH_HEALTH_DATA_SIZE,
                                    false
                                )
                            )

                        var nextPage: Int? = -1
                        do {
                            when (val copiedLoadResult = loadResult) {
                                is PagingSource.LoadResult.Page -> {
                                    if (copiedLoadResult.data.isEmpty() && nextPage == -1) {
                                        Log.e(TAG, "data is empty: $dataType")
                                        AppLogger.saveLog(DataSyncLog("nothing to sync $dataType"))
                                        finishSync(dao, lastTimestamp)
                                        break
                                    }

                                    nextPage = copiedLoadResult.nextKey

                                    if (copiedLoadResult.data.size == BuildConfig.BATCH_HEALTH_DATA_SIZE) {
                                        batchHealthData.add(copiedLoadResult.data)
                                    }

                                    if (batchHealthData.size != 0 && (nextPage == null || copiedLoadResult.data.size != BuildConfig.BATCH_HEALTH_DATA_SIZE || batchHealthData.size == BuildConfig.NUM_BATCH_HEALTH_DATA)) {
                                        grpcHealthDataSynchronizer.syncBatchHealthData(
                                            studyIds,
                                            batchHealthData.map {
                                                toHealthDataModel(dataType, it)
                                            },
                                        ).onSuccess {
                                            Log.i(TAG, "success to upload data: $dataType")
                                            AppLogger.saveLog(
                                                DataSyncLog(
                                                    "sync $dataType ${
                                                        batchHealthData.map { it.size }.sum()
                                                    }"
                                                )
                                            )
                                            lastTimestamp = batchHealthData.last().last().timestamp
                                            batchHealthData.clear()
                                        }.onFailure {
                                            Log.e(TAG, "fail to upload data to server")
                                            Log.e(TAG, it.stackTraceToString())
                                            AppLogger.saveLog(DataSyncLog("FAIL: sync data $dataType ${it.stackTraceToString()}"))
                                            finishSync(dao, lastTimestamp)
                                        }.getOrThrow()
                                    }
                                }

                                is PagingSource.LoadResult.Error -> {
                                    Log.e(TAG, copiedLoadResult.throwable.stackTraceToString())
                                    AppLogger.saveLog(DataSyncLog("FAIL: sync data $dataType ${copiedLoadResult.throwable.stackTraceToString()}"))
                                    finishSync(dao, lastTimestamp)
                                    throw copiedLoadResult.throwable
                                }

                                is PagingSource.LoadResult.Invalid -> {
                                    Log.e(TAG, "Invalid page")
                                    AppLogger.saveLog(DataSyncLog("FAIL: sync data $dataType Invalid page"))
                                    finishSync(dao, lastTimestamp)
                                    throw IOException("Invalid page")
                                }
                            }

                            if (nextPage == null) {
                                finishSync(dao, lastTimestamp)
                            } else {
                                loadResult = pageSource.load(
                                    PagingSource.LoadParams.Append(nextPage, BuildConfig.BATCH_HEALTH_DATA_SIZE, false)
                                )
                            }
                        } while (nextPage != null)
                    }

                    else -> {
                        var lastTimestamp = 0L
                        val pageSource = dao.getGreaterThan(0)

                        var loadResult =
                            pageSource.load(PagingSource.LoadParams.Refresh(null, PAGE_LOAD_SIZE, false))

                        var nextPage: Int? = -1
                        do {
                            when (val copiedLoadResult = loadResult) {
                                is PagingSource.LoadResult.Page -> {
                                    if (copiedLoadResult.data.isEmpty() && nextPage == -1) {
                                        Log.e(TAG, "data is empty: $dataType")
                                        AppLogger.saveLog(DataSyncLog("nothing to sync $dataType"))
                                        break
                                    }

                                    grpcHealthDataSynchronizer.syncHealthData(
                                        studyIds,
                                        toHealthDataModel(dataType, copiedLoadResult.data)
                                    ).onSuccess {
                                        Log.i(TAG, "success to upload data: $dataType")
                                        AppLogger.saveLog(DataSyncLog("sync $dataType ${copiedLoadResult.data.size}"))
                                        lastTimestamp = copiedLoadResult.data.last().timestamp
                                        nextPage = copiedLoadResult.nextKey
                                    }.onFailure {
                                        Log.e(TAG, "fail to upload data to server")
                                        Log.e(TAG, it.stackTraceToString())
                                        AppLogger.saveLog(DataSyncLog("FAIL: sync data $dataType ${it.stackTraceToString()}"))
                                        finishSync(dao, lastTimestamp)
                                    }.getOrThrow()
                                }

                                is PagingSource.LoadResult.Error -> {
                                    Log.e(TAG, copiedLoadResult.throwable.stackTraceToString())
                                    AppLogger.saveLog(DataSyncLog("FAIL: sync data $dataType ${copiedLoadResult.throwable.stackTraceToString()}"))
                                    finishSync(dao, lastTimestamp)
                                    throw copiedLoadResult.throwable
                                }

                                is PagingSource.LoadResult.Invalid -> {
                                    Log.e(TAG, "Invalid page")
                                    AppLogger.saveLog(DataSyncLog("FAIL: sync data $dataType Invalid page"))
                                    finishSync(dao, lastTimestamp)
                                    throw IOException("Invalid page")
                                }
                            }

                            if (nextPage == null) {
                                finishSync(dao, lastTimestamp)
                            } else {
                                loadResult = pageSource.load(
                                    PagingSource.LoadParams.Append(nextPage!!, PAGE_LOAD_SIZE, false)
                                )
                            }
                        } while (nextPage != null)
                    }
                }
            }.onFailure {
                check = false
                Log.e(TAG, it.stackTraceToString())
            }
        }
    }

    private fun <T : TimestampMapData> toHealthDataModel(dataType: PrivDataType, data: List<T>): HealthDataModel {
        return HealthDataModel(dataType, data.map { it.toDataMap() })
    }

    private fun <T : Timestamp> finishSync(dao: PrivDao<T>, lastSyncTime: Long) {
        dao.deleteLEThan(lastSyncTime)
    }

    companion object {
        private const val PAGE_LOAD_SIZE = 1000
        private val TAG = this::class.simpleName
    }
}
