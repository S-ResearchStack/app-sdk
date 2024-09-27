package researchstack.data.datasource.http

import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.ConnectException

class RetrofitClientTest {
    private lateinit var retrofitClient: RetrofitClient

    @BeforeEach
    fun setUp() {
        RetrofitClient.initialize()
        retrofitClient = spyk(RetrofitClient.getInstance(), recordPrivateCalls = true)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAPI should return FileUploadApi`() {
        Assertions.assertNotNull(retrofitClient.getAPI())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAPI#upload should throw Exception`() = runTest {
        val requestBody = ByteArrayInputStream("abc".toByteArray()).asRequestBody()
        runCatching {
            retrofitClient.getAPI().upload("test", requestBody)
        }.onFailure {
            Assertions.assertTrue(it is ConnectException)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAPI#uploadImage should throw Exception`() = runTest {
        val reqBody = ByteArrayInputStream("abc".toByteArray()).asRequestBody("application/png".toMediaTypeOrNull())
        val partImage = MultipartBody.Part.createFormData("image-file", "abc", reqBody)
        runCatching {
            retrofitClient.getAPI().uploadImage("test", partImage)
        }.onFailure {
            Assertions.assertTrue(it is ConnectException)
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
}
