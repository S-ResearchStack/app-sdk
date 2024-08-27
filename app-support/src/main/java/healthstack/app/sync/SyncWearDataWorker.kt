package healthstack.app.sync

import android.content.Context
import android.util.Log
import androidx.work.WorkerParameters
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.google.firebase.auth.FirebaseAuth
import healthstack.app.mapper.WearDataObjectMapper
import healthstack.backend.integration.BackendFacade
import healthstack.backend.integration.BackendFacadeHolder
import healthstack.common.HEALTH_DATA_FOLDER_NAME
import healthstack.common.model.Accelerometer
import healthstack.common.model.EcgSet
import healthstack.common.model.HeartRate
import healthstack.common.model.PpgGreen
import healthstack.common.model.WearData
import healthstack.common.model.WearDataType
import healthstack.healthdata.link.HealthData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.io.input.ReaderInputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader


class SyncWearDataWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : NetworkAwareWorker(appContext, workerParams) {
    private val healthDataSyncClient: BackendFacade = BackendFacadeHolder.getInstance()
    private val objectMapper = WearDataObjectMapper.getInstance()

    override suspend fun doTask(): Result {
        val dataDir = "${applicationContext.filesDir}" + HEALTH_DATA_FOLDER_NAME
        val dir = File(dataDir)

        dir.listFiles()?.forEach { file ->
            syncFile(file).onSuccess {
                Log.i(TAG, "mobile->backend ${file.name} success")
                file.delete()
            }
        }
        return Result.success()
    }

    private suspend fun syncFile(file: File): kotlin.Result<Unit> = runCatching {
        Log.i(TAG, "try to sync ${file.name} file to the server")

        CoroutineScope(Dispatchers.IO).launch {
            FileInputStream(file).use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val dataType = WearDataType.valueOf(reader.readLine())
                val data = convertToData(dataType, ReaderInputStream(reader))

                syncData(dataType, data.map { it.toDataMap() })
            }
        }.join()
    }

    private fun convertToData(dataType: WearDataType, csvInputStream: InputStream): List<WearData> {
        return  when(dataType) {
            WearDataType.WEAR_ACCELEROMETER -> readCsv<Accelerometer>(csvInputStream)
            WearDataType.WEAR_ECG -> readCsv<EcgSet>(csvInputStream)
            WearDataType.WEAR_HEART_RATE -> readCsv<HeartRate>(csvInputStream)
            WearDataType.WEAR_PPG_GREEN -> readCsv<PpgGreen>(csvInputStream)
        }
    }

    private inline fun <reified T> readCsv(inputStream: InputStream): List<T> {
        val csvMapper = CsvMapper().apply {
            enable(CsvParser.Feature.TRIM_SPACES)
            enable(CsvParser.Feature.SKIP_EMPTY_LINES)
        }

        val schema = CsvSchema.emptySchema().withHeader().withColumnSeparator('|')

        return csvMapper.readerFor(Map::class.java)
            .with(schema)
            .readValues<Map<String, String>>(inputStream)
            .readAll()
            .map {
                objectMapper.convertValue(it, T::class.java)
            }
    }

    private suspend fun syncData(dataType: WearDataType, data: List<Map<String, Any>>) {
        Log.i(TAG, "try to sync data. dataType: $dataType, size: ${data.size}")

        FirebaseAuth.getInstance().currentUser?.getIdToken(false)
            ?.addOnSuccessListener { result ->
                result.token?.let { idToken ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            healthDataSyncClient.syncHealthData(
                                idToken,
                                HealthData(
                                    dataType.name,
                                    data
                                )
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "fail to sync data")
                            e.printStackTrace()
                        }
                    }
                }
            }?.addOnFailureListener {
                Log.e(TAG, "fail to get id token")
            }
    }

    companion object {
        private val TAG = SyncWearDataWorker::class.simpleName
    }
}
