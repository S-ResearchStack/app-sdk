package healthstack.app

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import healthstack.app.pref.AppStage
import healthstack.kit.info.publication.PdfPublication
import healthstack.kit.info.publication.TextPublication
import healthstack.kit.info.publication.VideoPublication
import healthstack.kit.info.publication.content.ImageBlock
import healthstack.kit.info.publication.content.TextBlock
import io.mockk.coJustRun
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class EducationViewTest {
    @get:Rule
    val rule = createComposeRule()

    val navigationMock = mockk<(AppStage) -> Unit>()

    private val textPublicationWithCover = TextPublication(
        "https://loremflickr.com/cache/resized/65535_52579342719_2a2af8651d_n_320_240_nofilter.jpg",
        "Test Publication",
        "Test Text Publication With Cover",
        "Estimated Time",
        listOf(
            TextBlock("This is text with cover"),
            ImageBlock(
                listOf(
                    "https://loremflickr.com/cache/resized/65535_52579342719_2a2af8651d_n_320_240_nofilter.jpg"
                )
            ),
            ImageBlock(
                listOf(
                    "https://loremflickr.com/cache/resized/65535_52579342719_2a2af8651d_n_320_240_nofilter.jpg",
                    "https://loremflickr.com/cache/resized/65535_52039567857_283a75ff1a_n_320_240_nofilter.jpg"
                )
            )
        )
    )

    private val textPublicationWithoutCover = TextPublication(
        null,
        "Test Publication",
        "Test Text Publication Without Cover",
        "Estimated Time",
        listOf(
            TextBlock("This is text without cover"),
            ImageBlock(
                listOf(
                    "https://loremflickr.com/cache/resized/65535_52579342719_2a2af8651d_n_320_240_nofilter.jpg"
                )
            ),
            ImageBlock(
                listOf(
                    "https://loremflickr.com/cache/resized/65535_52579342719_2a2af8651d_n_320_240_nofilter.jpg",
                    "https://loremflickr.com/cache/resized/65535_52039567857_283a75ff1a_n_320_240_nofilter.jpg"
                )
            )
        )
    )

    private val pdfPublication = PdfPublication(
        "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
        "Test Publication",
        "Test PDF Publication",
        "Estimated Time",
        listOf(
            TextBlock("This is pdf")
        )
    )

    private val videoPublication = VideoPublication(
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "Test Publication",
        "Test Video Publication",
        "Estimated Time",
        listOf(
            TextBlock("This is video")
        )
    )

    private fun createEducationView(
        changeNavigation: (AppStage) -> Unit
    ): EducationView =
        EducationView(
            changeNavigation,
            listOf(textPublicationWithCover, textPublicationWithoutCover, pdfPublication, videoPublication)
        )

    @Test
    fun testDefaultEducationView() {
        coJustRun { navigationMock(any()) }
        val educationView = createEducationView(navigationMock)

        rule.setContent {
            educationView.Render()
        }

        rule.onNodeWithText("Test Video Publication").assertExists()
    }

    @Test
    fun testPublicationSelection() {
        coJustRun { navigationMock(any()) }
        val educationView = createEducationView(navigationMock)

        rule.setContent {
            educationView.Render()
        }

        rule.onNodeWithText("Test Text Publication With Cover")
            .performClick()

        rule.onNodeWithText("This is text with cover")
            .assertExists()
    }
}
