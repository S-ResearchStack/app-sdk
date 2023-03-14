package healthstack.kit.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    // Main Color Palette
    primary: Color,
    primaryVariant: Color,
    background: Color,
    surface: Color,
    onPrimary: Color,
    onBackground: Color,
    onSurface: Color,
    onSurfaceVariant: Color,
    error: Color,
    errorVariant: Color,
    onError: Color,
    disabled: Color,
    onDisabled: Color,
    disabled2: Color,
    onDisabled2: Color,
    // Data Visualization Color Palette
    dataVisualization1: Color,
    dataVisualization1Variant: Color,
    dataVisualization2: Color,
    dataVisualization2Variant: Color,
    dataVisualization3: Color,
    dataVisualization3Variant: Color,
    dataVisualization4: Color,
    dataVisualization4Variant: Color,
    dataVisualization5: Color,
    dataVisualization5Variant: Color,
    // Alert & Status Color Palette
    success: Color,
    successVariant: Color,
    warning: Color,
    warningVariant: Color,
    // Theme
    isLight: Boolean,
) {
    var primary by mutableStateOf(primary)
        private set

    var primaryVariant by mutableStateOf(primaryVariant)
        private set

    var background by mutableStateOf(background)
        private set

    var surface by mutableStateOf(surface)
        private set

    var onPrimary by mutableStateOf(onPrimary)
        private set

    var onBackground by mutableStateOf(onBackground)
        private set

    var onSurface by mutableStateOf(onSurface)
        private set

    var onSurfaceVariant by mutableStateOf(onSurfaceVariant)
        private set

    var error by mutableStateOf(error)
        private set

    var errorVariant by mutableStateOf(errorVariant)
        private set

    var onError by mutableStateOf(onError)
        private set

    var disabled by mutableStateOf(disabled)
        private set

    var onDisabled by mutableStateOf(onDisabled)
        private set

    var disabled2 by mutableStateOf(disabled2)
        private set

    var onDisabled2 by mutableStateOf(onDisabled2)
        private set

    var dataVisualization1 by mutableStateOf(dataVisualization1)
        private set

    var dataVisualization1Variant by mutableStateOf(dataVisualization1Variant)
        private set

    var dataVisualization2 by mutableStateOf(dataVisualization2)
        private set

    var dataVisualization2Variant by mutableStateOf(dataVisualization2Variant)
        private set

    var dataVisualization3 by mutableStateOf(dataVisualization3)
        private set

    var dataVisualization3Variant by mutableStateOf(dataVisualization3Variant)
        private set

    var dataVisualization4 by mutableStateOf(dataVisualization4)
        private set

    var dataVisualization4Variant by mutableStateOf(dataVisualization4Variant)
        private set

    var dataVisualization5 by mutableStateOf(dataVisualization5)
        private set

    var dataVisualization5Variant by mutableStateOf(dataVisualization5Variant)
        private set

    var success by mutableStateOf(success)
        private set

    var successVariant by mutableStateOf(successVariant)
        private set

    var warning by mutableStateOf(warning)
        private set

    var warningVariant by mutableStateOf(warningVariant)
        private set

    var isLight by mutableStateOf(isLight)
        private set

    fun copy(
        // Main Color Palette
        primary: Color = this.primary,
        primaryVariant: Color = this.primaryVariant,
        background: Color = this.background,
        surface: Color = this.surface,
        onPrimary: Color = this.onPrimary,
        onBackground: Color = this.onBackground,
        onSurface: Color = this.onSurface,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        error: Color = this.error,
        errorVariant: Color = this.errorVariant,
        onError: Color = this.onError,
        disabled: Color = this.disabled,
        onDisabled: Color = this.onDisabled,
        disabled2: Color = this.disabled2,
        onDisabled2: Color = this.onDisabled2,
        // Data Visualization Color Palette
        dataVisualization1: Color = this.dataVisualization1,
        dataVisualization1Variant: Color = this.dataVisualization1Variant,
        dataVisualization2: Color = this.dataVisualization2,
        dataVisualization2Variant: Color = this.dataVisualization2Variant,
        dataVisualization3: Color = this.dataVisualization3,
        dataVisualization3Variant: Color = this.dataVisualization3Variant,
        dataVisualization4: Color = this.dataVisualization4,
        dataVisualization4Variant: Color = this.dataVisualization4Variant,
        dataVisualization5: Color = this.dataVisualization5,
        dataVisualization5Variant: Color = this.dataVisualization5Variant,
        // Alert & Status Color Palette
        success: Color = this.success,
        successVariant: Color = this.successVariant,
        warning: Color = this.warning,
        warningVariant: Color = this.warningVariant,
        // Theme
        isLight: Boolean = this.isLight,
    ): AppColors = AppColors(
        // Main Color Palette
        primary,
        primaryVariant,
        background,
        surface,
        onPrimary,
        onBackground,
        onSurface,
        onSurfaceVariant,
        error,
        errorVariant,
        onError,
        disabled,
        onDisabled,
        disabled2,
        onDisabled2,
        // Data Visualization Color Palette
        dataVisualization1,
        dataVisualization1Variant,
        dataVisualization2,
        dataVisualization2Variant,
        dataVisualization3,
        dataVisualization3Variant,
        dataVisualization4,
        dataVisualization4Variant,
        dataVisualization5,
        dataVisualization5Variant,
        // Alert & Status Color Palette
        success,
        successVariant,
        warning,
        warningVariant,
        // Theme
        isLight
    )

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        primaryVariant = other.primaryVariant
        background = other.background
        surface = other.surface
        onPrimary = other.onPrimary
        onBackground = other.onBackground
        onSurface = other.onSurface
        onSurfaceVariant = other.onSurfaceVariant
        error = other.error
        errorVariant = other.errorVariant
        onError = other.onError
        disabled = other.disabled
        onDisabled = other.onDisabled
        disabled2 = other.disabled2
        onDisabled2 = other.onDisabled2
        // Data Visualization Color Palette
        dataVisualization1 = other.dataVisualization1
        dataVisualization1Variant = other.dataVisualization1Variant
        dataVisualization2 = other.dataVisualization2
        dataVisualization2Variant = other.dataVisualization2Variant
        dataVisualization3 = other.dataVisualization3
        dataVisualization3Variant = other.dataVisualization3Variant
        dataVisualization4 = other.dataVisualization4
        dataVisualization4Variant = other.dataVisualization4Variant
        dataVisualization5 = other.dataVisualization5
        dataVisualization5Variant = other.dataVisualization5Variant
        // Alert & Status Color Palette
        success = other.success
        successVariant = other.successVariant
        warning = other.warning
        warningVariant = other.warningVariant
        // Theme
        isLight = other.isLight
    }

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

internal val LocalColors = staticCompositionLocalOf { mainLightColors() }
