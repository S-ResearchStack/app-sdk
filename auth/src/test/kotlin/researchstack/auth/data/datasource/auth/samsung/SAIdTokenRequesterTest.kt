package researchstack.auth.data.datasource.auth.samsung

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import com.msc.sa.aidl.ISAService
import com.msc.sa.aidl.ISAService.Stub
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows
import researchstack.auth.NEGATIVE_TEST
import researchstack.auth.POSITIVE_TEST
import researchstack.auth.data.datasource.auth.SAIdTokenRequester
import researchstack.auth.data.datasource.auth.samsung.SACallbackHandler.Companion.ID_TOKEN_KEY

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(PER_CLASS)
internal class SAIdTokenRequesterTest {

    private val isaService = mockk<ISAService>()
    private val context = mockk<Context> {
        every { packageName } returns "researchstack"
    }

    private val serviceConnection = slot<ServiceConnection>()
    private val saCallbackHandler = slot<SACallbackHandler>()
    var tokenResult: Result<String>? = null

    @BeforeAll
    fun beforeAll() {
        mockkStatic(Stub::class)
        every { Stub.asInterface(any()) } returns isaService
    }

    @AfterAll
    fun afterAll() {
        unmockkAll()
    }

    @Order(1)
    @Tag(NEGATIVE_TEST)
    @Test
    fun `requestIdToken should throw IllegalStateException if service is not bind`() = runTest {
        assertThrows<IllegalStateException> {
            SAIdTokenRequester.requestIdToken()
        }
    }

    @Order(2)
    @Tag(POSITIVE_TEST)
    @Test
    fun `requestIdToken should call IdTokenListener with success result`() = runTest {
        bindService()

        val bundle = mockk<Bundle>()
        every { bundle.getString(ID_TOKEN_KEY) } returns "id-token"

        every { isaService.requestAuthCode(any(), any(), any()) } answers {
            saCallbackHandler.captured.onReceiveAuthCode(1, true, bundle)
            true
        }

        SAIdTokenRequester.requestIdToken()
        assertEquals(true, tokenResult?.isSuccess)
    }

    @Order(3)
    @Tag(NEGATIVE_TEST)
    @Test
    fun `requestIdToken should call IdTokenListener with failure if returned bundle do not have id token`() = runTest {
        bindService()

        val bundle = mockk<Bundle>()
        every { bundle.getString(ID_TOKEN_KEY) } returns null

        every { isaService.requestAuthCode(any(), any(), any()) } answers {
            saCallbackHandler.captured.onReceiveAuthCode(1, true, bundle)
            true
        }

        SAIdTokenRequester.requestIdToken()
        assertEquals(true, tokenResult?.isFailure)
    }

    @Order(4)
    @Tag(NEGATIVE_TEST)
    @Test
    fun `requestIdToken should call IdTokenListener with failure if isSuccess is false`() = runTest {
        bindService()

        val bundle = mockk<Bundle>()
        every { bundle.getString(any()) } returns null

        every { isaService.requestAuthCode(any(), any(), any()) } answers {
            saCallbackHandler.captured.onReceiveAuthCode(1, false, bundle)
            true
        }

        SAIdTokenRequester.requestIdToken()
        assertEquals(true, tokenResult?.isFailure)
    }

    @Order(5)
    @Tag(NEGATIVE_TEST)
    @Test
    fun `requestIdToken should disconnect service if onServiceDisconnected is called`() = runTest {
        every { isaService.unregisterCallback(any()) } returns true
        serviceConnection.captured.onServiceDisconnected(ComponentName("i", "b"))
        assertThrows<IllegalStateException> {
            SAIdTokenRequester.requestIdToken()
        }
    }

    private suspend fun bindService() {
        every { context.bindService(any(), capture(serviceConnection), any<Int>()) } returns true

        SAIdTokenRequester.initialize(context, "test-id", "test-secret")

        while (!serviceConnection.isCaptured) {
            delay(1)
        }

        every {
            isaService.registerCallback(
                any(), any(), any(), capture(saCallbackHandler)
            )
        } returns "register-code"

        serviceConnection.captured.onServiceConnected(
            ComponentName("componentName", "class"), mockk<Binder>()
        )

        SAIdTokenRequester.registerIdTokenListener(object : IdTokenListener {
            override fun onReceiveTokenResult(result: Result<String>) {
                tokenResult = result
            }
        })
    }
}
