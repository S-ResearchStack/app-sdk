package researchstack.domain.repository

import java.io.InputStream

interface FileRepository {
    suspend fun getPresignedUrl(
        fileName: String,
        studyId: String,
    ): Result<String>

    suspend fun uploadImage(
        presignedUrl: String,
        filePath: String,
    ): Result<Unit>

    suspend fun uploadFile(presignedUrl: String, inputStream: InputStream): Result<Unit>

    suspend fun saveFile(fileName: String, inputStream: InputStream): Result<Unit>

    suspend fun deleteFile(fileName: String): Result<Unit>
}
