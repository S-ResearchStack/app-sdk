package researchstack.data.repository

import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.source
import researchstack.data.datasource.grpc.GrpcFileDataSource
import researchstack.data.datasource.http.FileUploadApi
import researchstack.data.datasource.local.file.FileManager
import researchstack.domain.repository.FileRepository
import java.io.File
import java.io.InputStream

class FileRepositoryImpl(
    private val grpcFileDataSource: GrpcFileDataSource,
    private val fileUploadApi: FileUploadApi,
    private val fileManager: FileManager
) : FileRepository {

    override suspend fun getPresignedUrl(
        fileName: String,
        studyId: String,
    ): Result<String> =
        withContext(Dispatchers.IO) {
            grpcFileDataSource.getPresignedUrl(fileName, studyId)
                .onFailure {
                    Log.e(
                        TAG,
                        "fail to getPresignedUrlForInformedConsent : ${it.message}"
                    )
                }
        }

    override suspend fun uploadImage(presignedUrl: String, filePath: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val file = File(filePath)
                val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                val reqBody = file.asRequestBody(mime?.toMediaTypeOrNull())
                val partImage = MultipartBody.Part.createFormData(FORM_DATA_NAME, file.name, reqBody)
                fileUploadApi.uploadImage(presignedUrl, partImage)
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }
        }

    override suspend fun uploadFile(presignedUrl: String, inputStream: InputStream): Result<Unit> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                fileUploadApi.upload(presignedUrl, inputStream.asRequestBody())
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }
        }

    override suspend fun saveFile(fileName: String, inputStream: InputStream): Result<Unit> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                fileManager.saveFile(fileName, inputStream)
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }
        }

    override suspend fun deleteFile(fileName: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                fileManager.deleteFile(fileName)
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }
        }

    private fun InputStream.asRequestBody(contentType: MediaType? = null): RequestBody {
        return object : RequestBody() {
            override fun contentType() = contentType

            override fun contentLength() = available().toLong()

            override fun writeTo(sink: BufferedSink) {
                source().use { source -> sink.writeAll(source) }
            }
        }
    }

    companion object {
        private val TAG = FileRepositoryImpl::class.simpleName
        private const val FORM_DATA_NAME = "image-file"
    }
}
