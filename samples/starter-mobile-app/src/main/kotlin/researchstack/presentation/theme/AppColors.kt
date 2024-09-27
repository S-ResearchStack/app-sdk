@file:Suppress("LongParameterList", "MagicNumber")

package researchstack.presentation.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    // Main Color Palette
    val primary: Color,
    val primaryVariant: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val error: Color,
    val errorVariant: Color,
    val onError: Color,
    val disabled: Color,
    val onDisabled: Color,
    val disabled2: Color,
    val onDisabled2: Color,
    // Data Visualization Color Palette
    val dataVisualization1: Color,
    val dataVisualization1Variant: Color,
    val dataVisualization2: Color,
    val dataVisualization2Variant: Color,
    val dataVisualization3: Color,
    val dataVisualization3Variant: Color,
    val dataVisualization4: Color,
    val dataVisualization4Variant: Color,
    val dataVisualization5: Color,
    val dataVisualization5Variant: Color,
    // Alert & Status Color Palette
    val success: Color,
    val successVariant: Color,
    val warning: Color,
    val warningVariant: Color,
    // Theme
    val isLight: Boolean,
) {
    fun materialColors(): Colors =
        Colors(
            primary = primary,
            primaryVariant = primaryVariant,
            secondary = dataVisualization1,
            secondaryVariant = dataVisualization1Variant,
            background = background,
            surface = surface,
            error = error,
            onPrimary = onPrimary,
            onSecondary = surface,
            onBackground = onBackground,
            onSurface = onSurface,
            onError = onError,
            isLight = isLight,
        )
}

fun mainLightColors(
    // Main Color Palette
    primary: Color = Color(0xFF4475E3),
    primaryVariant: Color = Color(0xFFDAE3F9),
    background: Color = Color(0xFFFBFBFB),
    surface: Color = Color(0xFFFFFFFF),
    onPrimary: Color = Color(0xFFFFFFFF),
    onBackground: Color = Color(0xFF000000),
    onSurface: Color = Color(0xFF000000),
    onSurfaceVariant: Color = Color(0xFF4475E3),
    error: Color = Color(0xFFFF3333),
    errorVariant: Color = Color(0xFFD14343),
    onError: Color = Color(0xFFFFFFFF),
    disabled: Color = Color(0xFFD7D7D7),
    onDisabled: Color = Color(0xFFFFFFFF),
    disabled2: Color = Color(0xFFF8F8F8),
    onDisabled2: Color = Color(0xFF9A9A9A),
    // Data Visualization Color Palette
    dataVisualization1: Color = Color(0xFF944ED7),
    dataVisualization1Variant: Color = Color(0xFF751EC7),
    dataVisualization2: Color = Color(0xFFF39C18),
    dataVisualization2Variant: Color = Color(0xFFC17A0D),
    dataVisualization3: Color = Color(0xFF00B0D7),
    dataVisualization3Variant: Color = Color(0xFF0D7491),
    dataVisualization4: Color = Color(0xFF1AD598),
    dataVisualization4Variant: Color = Color(0xFF00A670),
    dataVisualization5: Color = Color(0xFFFA5F4F),
    dataVisualization5Variant: Color = Color(0xFFCC4E40),
    // Alert & Status Color Palette
    success: Color = Color(0xFF2DC008),
    successVariant: Color = Color(0xFF00825D),
    warning: Color = Color(0xFFF5BF00),
    warningVariant: Color = Color(0xFFA36400),
    // Theme
    isLight: Boolean = true,
): AppColors = AppColors(
    primary = primary,
    primaryVariant = primaryVariant,
    background = background,
    surface = surface,
    onPrimary = onPrimary,
    onBackground = onBackground,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceVariant,
    error = error,
    errorVariant = errorVariant,
    onError = onError,
    disabled = disabled,
    onDisabled = onDisabled,
    disabled2 = disabled2,
    onDisabled2 = onDisabled2,
    // Data Visualization Color Palette,
    dataVisualization1 = dataVisualization1,
    dataVisualization1Variant = dataVisualization1Variant,
    dataVisualization2 = dataVisualization2,
    dataVisualization2Variant = dataVisualization2Variant,
    dataVisualization3 = dataVisualization3,
    dataVisualization3Variant = dataVisualization3Variant,
    dataVisualization4 = dataVisualization4,
    dataVisualization4Variant = dataVisualization4Variant,
    dataVisualization5 = dataVisualization5,
    dataVisualization5Variant = dataVisualization5Variant,
    // Alert & Status Color Palette,
    success = success,
    successVariant = successVariant,
    warning = warning,
    warningVariant = warningVariant,
    // Theme
    isLight = isLight
)

fun mainDarkColors(
    // Main Color Palette
    primary: Color = Color(0xFF68A8FF),
    primaryVariant: Color = Color(0xFF222F41),
    background: Color = Color(0xFF0E0E0E),
    surface: Color = Color(0xFF232527),
    onPrimary: Color = Color(0xFF000000),
    onBackground: Color = Color(0xFFFFFFFF),
    onSurface: Color = Color(0xFFFFFFFF),
    onSurfaceVariant: Color = Color(0xFF68A8FF),
    error: Color = Color(0xFFFD7278),
    errorVariant: Color = Color(0xFFFFCED7),
    onError: Color = Color(0xFF000000),
    disabled: Color = Color(0xFF4E4E4E),
    onDisabled: Color = Color(0xFFFFFFFF),
    disabled2: Color = Color(0xFF1D1D1D),
    onDisabled2: Color = Color(0xFF737373),
    // Data Visualization Color Palette
    dataVisualization1: Color = Color(0xFFDB5AEE),
    dataVisualization1Variant: Color = Color(0xFFEFBDF7),
    dataVisualization2: Color = Color(0xFFFFB536),
    dataVisualization2Variant: Color = Color(0xFFFFDFAB),
    dataVisualization3: Color = Color(0xFF09C8FE),
    dataVisualization3Variant: Color = Color(0xFFACE8FE),
    dataVisualization4: Color = Color(0xFF1AD598),
    dataVisualization4Variant: Color = Color(0xFFB3EDD2),
    dataVisualization5: Color = Color(0xFF1AD598),
    dataVisualization5Variant: Color = Color(0xFFFFD1D3),
    // Alert & Status Color Palette
    success: Color = Color(0xFF74C76A),
    successVariant: Color = Color(0xFFCEDACA),
    warning: Color = Color(0xFFEECE55),
    warningVariant: Color = Color(0xFFF7E8B4),
    // Theme
    isLight: Boolean = false,
): AppColors = AppColors(
    primary = primary,
    primaryVariant = primaryVariant,
    background = background,
    surface = surface,
    onPrimary = onPrimary,
    onBackground = onBackground,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceVariant,
    error = error,
    errorVariant = errorVariant,
    onError = onError,
    disabled = disabled,
    onDisabled = onDisabled,
    disabled2 = disabled2,
    onDisabled2 = onDisabled2,
    // Data Visualization Color Palette,
    dataVisualization1 = dataVisualization1,
    dataVisualization1Variant = dataVisualization1Variant,
    dataVisualization2 = dataVisualization2,
    dataVisualization2Variant = dataVisualization2Variant,
    dataVisualization3 = dataVisualization3,
    dataVisualization3Variant = dataVisualization3Variant,
    dataVisualization4 = dataVisualization4,
    dataVisualization4Variant = dataVisualization4Variant,
    dataVisualization5 = dataVisualization5,
    dataVisualization5Variant = dataVisualization5Variant,
    // Alert & Status Color Palette,
    success = success,
    successVariant = successVariant,
    warning = warning,
    warningVariant = warningVariant,
    // Theme
    isLight = isLight
)

internal val LocalColors = staticCompositionLocalOf { mainLightColors() }

// TODO often used. but not defined in colors
val descriptionColor = Color(0x99000000)
