package researchstack.data.local.file

import android.content.Context
import android.text.format.DateUtils
import com.google.gson.Gson
import researchstack.HEALTH_DATA_FOLDER_NAME
import researchstack.domain.model.Timestamp
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

open class FileRepository<T : Timestamp>(
    private val kClass: KClass<T>,
    open val context: Context,
    open val splitInterval: Long = DateUtils.HOUR_IN_MILLIS
) {
    val gson = Gson()

    fun saveAll(data: Collection<T>) {
        val outputDir = "${context.filesDir}" + HEALTH_DATA_FOLDER_NAME

        Files.createDirectories(Paths.get(outputDir))

        data.groupBy { it.timestamp / splitInterval * splitInterval }.forEach { (timestamp, group) ->
            val file = File(outputDir, "$timestamp-${timestamp + splitInterval}-${kClass.simpleName}.csv")

            val healthData = group.joinToString("\n") { target ->
                target.javaClass.kotlin.memberProperties.joinToString("|") {
                    gson.toJson(it.get(target))
                }
            }

            runCatching {
                if (!file.exists()) {
                    FileOutputStream(file, true).use { output ->
                        DeviceStatDataType.fromModel(kClass)?.let {
                            output.write(it.name.toByteArray() + "\n".toByteArray())
                        } ?: run {
                            output.write(PrivDataType.fromModel(kClass).name.toByteArray() + "\n".toByteArray())
                        }
                        output.write(
                            kClass.memberProperties.joinToString("|") { it.name }
                                .toByteArray() + "\n".toByteArray()
                        )
                    }
                }
                FileOutputStream(file, true).use { output ->
                    output.write(healthData.toByteArray() + "\n".toByteArray())
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getCompletedFiles(): List<File> {
        val dataDir = "${context.filesDir}" + HEALTH_DATA_FOLDER_NAME
        val allFiles = File(dataDir).listFiles() ?: emptyArray()

        val dataFiles = allFiles.filter { it.name.contains(kClass.simpleName!!) }

        val (files, outdatedFiles) = dataFiles.partition {
            it.measuredTime() >= System.currentTimeMillis() - TimeUnit.DAYS.toMillis(90)
        }

        outdatedFiles.onEach { it.delete() }

        return files.filter { it.measuredTime() < System.currentTimeMillis() / splitInterval * splitInterval - splitInterval }
    }

    private fun File.measuredTime() = this.name.split("-")[0].toLong()

    fun deleteFile(): Result<Unit> {
        return kotlin.runCatching {
            val dataDir = "${context.filesDir}" + HEALTH_DATA_FOLDER_NAME
            val allFiles = File(dataDir).listFiles() ?: emptyArray()

            val dataFiles = allFiles.filter { it.name.contains(kClass.simpleName!!) }
            dataFiles.onEach { it.delete() }
        }
    }
}
