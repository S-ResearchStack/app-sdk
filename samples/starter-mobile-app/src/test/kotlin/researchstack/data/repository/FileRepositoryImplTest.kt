package researchstack.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import io.grpc.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.integration.outport.FileOutPort
import researchstack.data.datasource.grpc.GrpcFileDataSource
import researchstack.data.datasource.http.FileUploadApi
import researchstack.data.datasource.local.file.FileManager
import retrofit2.HttpException
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection.HTTP_FORBIDDEN

internal class FileRepositoryImplTest {

    private val fileOutPort = mockk<FileOutPort>()
    private val uploadApi = mockk<FileUploadApi>()
    private val context = mockk<Context>()
    private val fileManager = FileManager(context)
    private val fileUploadRepository = FileRepositoryImpl(
        GrpcFileDataSource(fileOutPort), uploadApi, fileManager
    )

    @TempDir
    private var tempDir: File? = null

    @BeforeEach
    fun beforeEach() {
        mockkStatic(Uri::class)
        val uri = mockk<Uri>()
        every { Uri.fromFile(any()) } returns uri
        every { uri.toString() } returns "ddd"

        mockkStatic(MimeTypeMap::class)
        every { MimeTypeMap.getFileExtensionFromUrl(any()) } returns "image/png"
        every { MimeTypeMap.getSingleton() } returns mockk {
            every { getMimeTypeFromExtension(any()) } returns "image/png"
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `getPresignedUrl should return success with url when argument is valid`() = runTest {
        val presignedUrl = "https://presigned-url.com"
        coEvery { fileOutPort.getPresignedUrl(any(), any()) } returns Result.success(presignedUrl)

        val result = fileUploadRepository.getPresignedUrl("filename", "study-id")
        assertTrue(result.isSuccess)
        assertEquals(presignedUrl, result.getOrNull())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `getPresignedUrl should return failure with exception if grpc throws error`() = runTest { //
        coEvery { fileOutPort.getPresignedUrl(any(), any()) } returns Result.failure(Status.UNAVAILABLE.asException())

        val result = fileUploadRepository.getPresignedUrl("filename", "study-id")
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `uploadFile should return success`() = runTest {
        val presignedUrl = "https://presigned-url.com"
        coEvery { uploadApi.upload(presignedUrl, any()) } returns Unit

        val result = fileUploadRepository.uploadFile(presignedUrl, InputStream.nullInputStream())
        assertTrue(result.isSuccess)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `uploadFile should return failure if storage service throw error`() = runTest {
        val presignedUrl = "https://presigned-url.com"
        coEvery { uploadApi.upload(presignedUrl, any()) } throws HttpException(
            Response.error<String>(HTTP_FORBIDDEN, "error".toResponseBody())
        )

        val result = fileUploadRepository.uploadFile(presignedUrl, InputStream.nullInputStream())
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `uploadImage should return success`() = runTest {
        val presignedUrl = "https://presigned-url.com"

        val file = createTestFile()

        coEvery { uploadApi.uploadImage(presignedUrl, any()) } returns Unit

        val result = fileUploadRepository.uploadImage(presignedUrl, file.absolutePath)
        assertTrue(result.isSuccess)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `uploadImage should return failure if storage service throw error`() = runTest {
        val presignedUrl = "https://presigned-url.com"
        coEvery { uploadApi.uploadImage(presignedUrl, any()) } throws HttpException(
            Response.error<String>(HTTP_FORBIDDEN, "error".toResponseBody())
        )
        val file = createTestFile()
        val result = fileUploadRepository.uploadImage(presignedUrl, file.absolutePath)
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `saveFile should return success`() = runTest {
        val inputStream = ByteArrayInputStream("abc".toByteArray())
        every { context.filesDir } returns tempDir
        val result = fileUploadRepository.saveFile("test", inputStream)
        assertTrue(result.isSuccess)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `saveFile should return failure`() = runTest {
        val inputStream = ByteArrayInputStream("abc".toByteArray())
        every { context.filesDir } throws Exception()
        val result = fileUploadRepository.saveFile("test", inputStream)
        assertTrue(result.isFailure)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `deleteFile should return success`() = runTest {
        every { context.filesDir } returns tempDir
        val result = fileUploadRepository.deleteFile("test")
        assertTrue(result.isSuccess)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `deleteFile should return failure`() = runTest {
        every { context.filesDir } throws Exception()
        val result = fileUploadRepository.deleteFile("test")
        assertTrue(result.isFailure)
    }

    private fun createTestFile(): File {
        val file = File(tempDir, "test")
        file.createNewFile()
        file.writeText("image-data")
        return file
    }
}
