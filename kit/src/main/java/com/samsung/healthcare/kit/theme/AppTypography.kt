package com.samsung.healthcare.kit.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.samsung.healthcare.kit.R

val Lato: FontFamily = FontFamily(
    Font(R.font.lato_regular),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.lato_thin, FontWeight.Thin),
    Font(R.font.lato_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.lato_black, FontWeight.Black),
    Font(R.font.lato_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_light_italic, FontWeight.Light, FontStyle.Italic),
)

val OpenSans: FontFamily = FontFamily(
    Font(R.font.open_sans_regular),
    Font(R.font.open_sans_bold, FontWeight.Bold),
    Font(R.font.open_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.open_sans_medium, FontWeight.Medium),
    Font(R.font.open_sans_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.open_sans_light, FontWeight.Light),
    Font(R.font.open_sans_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.open_sans_semi_bold, FontWeight.SemiBold),
    Font(R.font.open_sans_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
)

data class AppTypography(
    var appTitle: TextStyle = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        fontSize = 40.sp
    ),
    val title1: TextStyle = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Italic,
        fontSize = 40.sp
    ),
    val title2: TextStyle = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp
    ),
    val title3: TextStyle = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Italic,
        fontSize = 20.sp
    ),

    val subHeader1: TextStyle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        fontSize = 18.sp
    ),

    val subHeader2: TextStyle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.W500,
        fontStyle = FontStyle.Italic,
        fontSize = 20.sp
    ),

    val body1: TextStyle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        fontSize = 16.sp
    ),

    val body2: TextStyle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        fontSize = 14.sp
    ),

    val body3: TextStyle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.W400,
        fontStyle = FontStyle.Normal,
        fontSize = 12.sp
    ),
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
