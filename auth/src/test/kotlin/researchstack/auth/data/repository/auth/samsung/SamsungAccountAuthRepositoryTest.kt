package researchstack.auth.data.repository.auth.samsung

import android.accounts.AccountManager
import android.content.Context
import android.net.Uri
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import researchstack.auth.NEGATIVE_TEST
import researchstack.auth.POSITIVE_TEST
import researchstack.auth.data.datasource.auth.SAIdTokenRequester
import researchstack.auth.data.datasource.auth.samsung.IdTokenListener
import researchstack.auth.domain.model.OidcAuthentication
import java.time.Instant

@TestInstance(PER_CLASS)
internal class SamsungAccountAuthRepositoryTest {
    private val context = mockk<Context>()
    private val idTokenListener = slot<IdTokenListener>()
    private val samsungAccountAuthRepository by lazy { SamsungAccountAuthRepository(context, "test-id") }

    private val accountManager = mockk<AccountManager>()

    private val subject = "test-subject"
    private val jwtToken = JWT.create()
        .withSubject(subject)
        .withExpiresAt(Instant.now().plusSeconds(1000))
        .sign(Algorithm.none())

    @BeforeAll
    fun beforeAll() {
        mockkObject(SAIdTokenRequester)
        mockkStatic(AccountManager::class)
        justRun { SAIdTokenRequester.registerIdTokenListener(capture(idTokenListener)) }
    }

    @AfterAll
    fun afterAll() {
        unmockkAll()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `signIn should return failure if user did not login with samsung account`() = runTest {
        every { AccountManager.get(any()) } returns accountManager
        every { accountManager.getAccountsByType(any()) } returns arrayOf()

        assertThrows<IllegalStateException> {
            samsungAccountAuthRepository.signIn(OidcAuthentication)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSamsungAccountByProvider should return token`() = runTest {
        val accountName = "account-name"
        every { context.contentResolver } returns mockk {
            every { call(any<Uri>(), any(), any(), any()) } returns mockk {
                every { getInt(any(), any()) } returns 0
                every { getString(any(), any()) } returns accountName
            }
        }

        val result = samsungAccountAuthRepository.getSamsungAccountByProvider(context)
        assertEquals(accountName, result)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getSamsungAccountByProvider should throw IllegalAccessException if result code is not 0`() = runTest {
        every { context.contentResolver } returns mockk {
            every { call(any<Uri>(), any(), any(), any()) } returns mockk {
                every { getInt(any(), any()) } returns 1
                every { getString(any(), any()) } returns "some"
            }
        }

        assertThrows<IllegalAccessException> {
            samsungAccountAuthRepository.getSamsungAccountByProvider(context)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getSamsungAccountByProvider should throw IllegalStateException if result message is empty`() = runTest {
        every { context.contentResolver } returns mockk {
            every { call(any<Uri>(), any(), any(), any()) } returns mockk {
                every { getInt(any(), any()) } returns 0
                every { getString(any(), any()) } returns ""
            }
        }

        assertThrows<IllegalStateException> {
            samsungAccountAuthRepository.getSamsungAccountByProvider(context)
        }
    }
}
