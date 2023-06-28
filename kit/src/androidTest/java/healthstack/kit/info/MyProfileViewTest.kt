package healthstack.kit.info

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainDarkColors
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Rule
import org.junit.Test

class MyProfileViewTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun settingsViewTest() {
        mockkStatic(FirebaseAuth::class)
        val firebaseAuth = mockk<FirebaseAuth>()
        val firebaseUser = mockk<FirebaseUser>()

        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.displayName } returns "samsung kim"
        every { firebaseUser.email } returns "test@samsung.com"

        rule.setContent {
            AppTheme(mainDarkColors()) {
                MyProfileView().Render()
            }
        }

        rule.onNodeWithText("Profile")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
