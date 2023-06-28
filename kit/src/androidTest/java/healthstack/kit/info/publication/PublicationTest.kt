package healthstack.kit.info.publication

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import healthstack.kit.info.publication.content.ImageBlock
import healthstack.kit.info.publication.content.TextBlock
import org.junit.Rule
import org.junit.Test

class PublicationTest {
    @get:Rule
    val rule = createComposeRule()

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

    @Test
    fun testTextPublicationWithCover() {
        rule.setContent {
            textPublicationWithCover.Render {}
        }

        rule.onNodeWithText("Test Text Publication With Cover")
            .performClick()

        rule.onNodeWithText("This is text with cover")
            .assertExists()
    }

    @Test
    fun testTextPublicationWithoutCover() {
        rule.setContent {
            textPublicationWithoutCover.Render {}
        }

        rule.onNodeWithText("Test Text Publication Without Cover")
            .performClick()

        rule.onNodeWithText("This is text without cover")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }

    @Test
    fun testPdfPublication() {
        rule.setContent {
            pdfPublication.Render {}
        }

        rule.onNodeWithText("Test PDF Publication")
            .performClick()

        rule.onNodeWithText("This is pdf")
            .assertExists()

        rule.onNodeWithText("View PDF")
            .performClick()

        rule.onNodeWithTag("PDF Progress Indicator")
            .assertExists()

        rule.waitUntil(30000L) {
            rule.onAllNodesWithTag("PDF Progress Indicator")
                .fetchSemanticsNodes().isEmpty()
        }

        rule.onNodeWithTag("PDF Page Count")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()

        rule.onNodeWithText("This is pdf")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }

    @Test
    fun testVideoPublication() {
        rule.setContent {
            videoPublication.Render {}
        }

        rule.onNodeWithText("Test Video Publication")
            .performClick()

        rule.onNodeWithText("This is video")
            .assertExists()

        rule.onNodeWithContentDescription("Mute/Unmute")
            .performClick()

        rule.onNodeWithContentDescription("Play/Pause")
            .performClick()

        rule.onNodeWithTag("Custom Video Controls")
            .assertIsDisplayed()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
