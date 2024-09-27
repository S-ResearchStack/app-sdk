package researchstack.data.repository

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.pref.DataStoreTestHelper
import researchstack.data.local.pref.UserProfilePref
import researchstack.domain.model.Gender
import researchstack.domain.model.UserProfile
import java.io.File

internal class UserProfileRepositoryImplTest : DataStoreTestHelper("profile.preferences_pb") {
    private val userProfilePref = UserProfilePref(testDataStore)
    private val userProfileRepositoryImpl =
        UserProfileRepositoryImpl(userProfilePref)

    @Before
    fun beforeEach() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `get measure time value should return null`() = runTest {
        File(tempDir, fileName).apply {
            deleteRecursively()
        }
        this.backgroundScope.launch {
            userProfileRepositoryImpl.getUserProfile().collect {
                Assertions.assertNull(it)
            }
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `save and get measure time value should be success`() = runTest {
        userProfileRepositoryImpl.save(UserProfile(1f, 1f, 10, Gender.MALE))
        val result = userProfileRepositoryImpl.getUserProfile()
        Assertions.assertNotNull(result)
    }
}
