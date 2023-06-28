package healthstack.kit.ui

import androidx.compose.ui.graphics.Color
import healthstack.kit.theme.AppColors
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("AppColors Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppColorsTest {
    @Tag("positive")
    @Test
    fun `get test`() {
        val appColors = AppColors(
            Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            Color.White, Color.White,
            true
        )

        Assertions.assertEquals(appColors.errorVariant, Color.White)
        Assertions.assertEquals(appColors.onError, Color.White)
        Assertions.assertEquals(appColors.onDisabled, Color.White)
        Assertions.assertEquals(appColors.disabled2, Color.White)
        Assertions.assertEquals(appColors.onDisabled2, Color.White)
        Assertions.assertEquals(appColors.dataVisualization1, Color.White)
        Assertions.assertEquals(appColors.dataVisualization1Variant, Color.White)
        Assertions.assertEquals(appColors.dataVisualization2, Color.White)
        Assertions.assertEquals(appColors.dataVisualization2Variant, Color.White)
        Assertions.assertEquals(appColors.dataVisualization3, Color.White)
        Assertions.assertEquals(appColors.dataVisualization3Variant, Color.White)
        Assertions.assertEquals(appColors.dataVisualization4, Color.White)
        Assertions.assertEquals(appColors.dataVisualization4Variant, Color.White)
        Assertions.assertEquals(appColors.dataVisualization5, Color.White)
        Assertions.assertEquals(appColors.dataVisualization5Variant, Color.White)
        Assertions.assertEquals(appColors.successVariant, Color.White)
        Assertions.assertEquals(appColors.warning, Color.White)
        Assertions.assertEquals(appColors.warningVariant, Color.White)
        Assertions.assertEquals(appColors.isLight, true)
        Assertions.assertEquals(appColors.background, Color.White)
    }
}
