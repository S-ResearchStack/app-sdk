package com.samsung.healthcare.kit.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    // MaterialTheme Color
    primary: Color,
    primaryVariant: Color,
    secondary: Color,
    secondaryVariant: Color,
    background: Color,
    surface: Color,
    error: Color,
    onPrimary: Color,
    onSecondary: Color,
    onBackground: Color,
    onSurface: Color,
    onError: Color,
    isLight: Boolean,
    // Extension
    lightBackground: Color,
    primaryDark: Color,
    textPrimary: Color,
    textPrimaryAccent: Color,
    textSecondary: Color,
    textHint: Color,
    border: Color,
) {
    var primary by mutableStateOf(primary)
        private set

    var primaryVariant by mutableStateOf(primaryVariant)
        private set

    var secondary by mutableStateOf(secondary)
        private set

    var secondaryVariant by mutableStateOf(secondaryVariant)
        private set

    var background by mutableStateOf(background)
        private set

    var surface by mutableStateOf(surface)
        private set

    var error by mutableStateOf(error)
        private set

    var onPrimary by mutableStateOf(onPrimary)
        private set

    var onSecondary by mutableStateOf(onSecondary)
        private set

    var onBackground by mutableStateOf(onBackground)
        private set

    var onSurface by mutableStateOf(onSurface)
        private set

    var onError by mutableStateOf(onError)
        private set

    var isLight by mutableStateOf(isLight)
        private set

    var lightBackground by mutableStateOf(lightBackground)
        private set

    var primaryDark by mutableStateOf(primaryDark)
        private set

    var textPrimary by mutableStateOf(textPrimary)
        private set

    var textPrimaryAccent by mutableStateOf(textPrimaryAccent)
        private set

    var textSecondary by mutableStateOf(textSecondary)
        private set

    var textHint by mutableStateOf(textHint)
        private set

    var border by mutableStateOf(border)
        private set

    fun copy(
        primary: Color = this.primary,
        primaryVariant: Color = this.primaryVariant,
        secondary: Color = this.secondary,
        secondaryVariant: Color = this.secondaryVariant,
        background: Color = this.background,
        surface: Color = this.surface,
        error: Color = this.error,
        onPrimary: Color = this.onPrimary,
        onSecondary: Color = this.onSecondary,
        onBackground: Color = this.onBackground,
        onSurface: Color = this.onSurface,
        onError: Color = this.onError,
        isLight: Boolean = this.isLight,
        lightBackground: Color = this.lightBackground,
        primaryDark: Color = this.primaryDark,
        textPrimary: Color = this.textPrimary,
        textPrimaryAccent: Color = this.textPrimaryAccent,
        textSecondary: Color = this.textSecondary,
        textHint: Color = this.textHint,
        border: Color = this.border,
    ): AppColors = AppColors(
        primary,
        primaryVariant,
        secondary,
        secondaryVariant,
        background,
        surface,
        error,
        onPrimary,
        onSecondary,
        onBackground,
        onSurface,
        onError,
        isLight,
        lightBackground,
        primaryDark,
        textPrimary,
        textPrimaryAccent,
        textSecondary,
        textHint,
        border,
    )

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        primaryVariant = other.primaryVariant
        secondary = other.secondary
        secondaryVariant = other.secondaryVariant
        background = other.background
        surface = other.surface
        error = other.error
        onPrimary = other.onPrimary
        onSecondary = other.onSecondary
        onBackground = other.onBackground
        onSurface = other.onSurface
        onError = other.onError
        isLight = other.isLight
        lightBackground = other.lightBackground
        primaryDark = other.primaryDark
        textPrimary = other.textPrimary
        textPrimaryAccent = other.textPrimaryAccent
        textSecondary = other.textPrimary
        textHint = other.textHint
        border = other.border
    }

    fun materialColors(): Colors =
        Colors(
            primary = primary,
            primaryVariant = primaryVariant,
            secondary = secondary,
            secondaryVariant = secondaryVariant,
            background = background,
            surface = surface,
            error = error,
            onPrimary = onPrimary,
            onSecondary = onSecondary,
            onBackground = onBackground,
            onSurface = onSurface,
            onError = onError,
            isLight = isLight,
        )
}

fun lightColors(
    // FIXME should set light color
    primary: Color = Color(0xFFFF9E00),
    primaryVariant: Color = Color(0xFFFF9E00),
    secondary: Color = Color(0xFFFF9E00),
    secondaryVariant: Color = Color(0xFFFF9E00),
    background: Color = Color(0xFFFFFFFF),
    surface: Color = Color(0xFFFFFFFF),
    error: Color = Color(0xFFFF9E00),
    onPrimary: Color = Color(0xFFFF9E00),
    onSecondary: Color = Color(0xFFFF9E00),
    onBackground: Color = Color(0xFFFFFFFF),
    onSurface: Color = Color(0xFFFF9E00),
    onError: Color = Color(0xFFFF9E00),
    isLight: Boolean = true,
    lightBackground: Color = Color(0xFFFFFFFF),
    primaryDark: Color = Color(0xFF443F36),
    textPrimary: Color = Color(0xFF130C00),
    textPrimaryAccent: Color = Color(0xFFFF9E00),
    textSecondary: Color = Color(0xFF443F36),
    textHint: Color = Color(0xFF746B5C),
    border: Color = Color(0xFF343A40),
): AppColors = AppColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError,
    isLight = isLight,
    lightBackground = lightBackground,
    primaryDark = primaryDark,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textPrimaryAccent = textPrimaryAccent,
    textHint = textHint,
    border = border
)

fun darkColors(
    // FIXME should set dark color
    primary: Color = Color(0xFFFF9E00),
    primaryVariant: Color = Color(0xFFFF9E00),
    secondary: Color = Color(0xFFFF9E00),
    secondaryVariant: Color = Color(0xFFFF9E00),
    background: Color = Color(0xFF141414),
    surface: Color = Color(0xFF2E2E2E),
    error: Color = Color(0xFFFF9E00),
    onPrimary: Color = Color(0xFFFF9E00),
    onSecondary: Color = Color(0xFFFF9E00),
    onBackground: Color = Color(0xFFFFFFFF),
    onSurface: Color = Color(0xFFFF9E00),
    onError: Color = Color(0xFFFF9E00),
    isLight: Boolean = true,
    lightBackground: Color = Color(0xFF5A5A5A),
    primaryDark: Color = Color(0xFFFF9E00),
    textPrimary: Color = Color(0xFFFFFFFF),
    textPrimaryAccent: Color = Color(0xFFFF9E00),
    textSecondary: Color = Color(0xFFFFFFFF),
    textHint: Color = Color(0xFFADA597),
    border: Color = Color(0xFFFFFFFF),
): AppColors = AppColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError,
    isLight = isLight,
    lightBackground = lightBackground,
    primaryDark = primaryDark,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textPrimaryAccent = textPrimaryAccent,
    textHint = textHint,
    border = border,
)

fun blueColors(
    primary: Color = Color(0xFF4475E3),
    primaryVariant: Color = Color(0xFFB3C6F1),
    secondary: Color = Color(0xFF4475E3),
    secondaryVariant: Color = Color(0xFFE3EAFB),
    background: Color = Color(0xFFFFFFFF),
    surface: Color = Color(0xFFFFFFFF),
    error: Color = Color(0xFF4475E3),
    onPrimary: Color = Color(0xFF4475E3),
    onSecondary: Color = Color(0xFF4475E3),
    onBackground: Color = Color(0xFFFFFFFF),
    onSurface: Color = Color(0xFF2D2D2D),
    onError: Color = Color(0xFF4475E3),
    isLight: Boolean = true,
    lightBackground: Color = Color(0xFFF7F8FA),
    primaryDark: Color = Color(0xFF68A8FF),
    textPrimary: Color = Color(0xFF474747),
    textPrimaryAccent: Color = Color(0xFF4475E3),
    textSecondary: Color = Color(0xFFFFFFFF),
    textHint: Color = Color(0xFF808080),
    border: Color = Color(0xFFB3C6F1),
): AppColors = AppColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError,
    isLight = isLight,
    lightBackground = lightBackground,
    primaryDark = primaryDark,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textPrimaryAccent = textPrimaryAccent,
    textHint = textHint,
    border = border
)

fun darkBlueColors(
    primary: Color = Color(0xFF68A8FF),
    primaryVariant: Color = Color(0xFF68A8FF),
    secondary: Color = Color(0xFF68A8FF),
    secondaryVariant: Color = Color(0xFF68A8FF),
    background: Color = Color(0xFF1E1E1E),
    surface: Color = Color(0xFF232527),
    error: Color = Color(0xFF4475E3),
    onPrimary: Color = Color(0xFF68A8FF),
    onSecondary: Color = Color(0xFF4475E3),
    onBackground: Color = Color(0xFFFFFFFF),
    onSurface: Color = Color(0xFFEEEEEE),
    onError: Color = Color(0xFF4475E3),
    isLight: Boolean = false,
    lightBackground: Color = Color(0xFF232527),
    primaryDark: Color = Color(0xFF68A8FF),
    textPrimary: Color = Color(0xFFFFFFFF),
    textPrimaryAccent: Color = Color(0xFF4475E3),
    textSecondary: Color = Color(0xFF232527),
    textHint: Color = Color(0xFF979797),
    border: Color = Color(0xFF4475E3),
): AppColors = AppColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError,
    isLight = isLight,
    lightBackground = lightBackground,
    primaryDark = primaryDark,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textPrimaryAccent = textPrimaryAccent,
    textHint = textHint,
    border = border
)

internal val LocalColors = staticCompositionLocalOf { blueColors() }
