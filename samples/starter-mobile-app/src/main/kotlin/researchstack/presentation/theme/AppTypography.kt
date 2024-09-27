package researchstack.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import researchstack.R

val Inter: FontFamily = FontFamily(
    Font(R.font.inter_regular),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extra_bold, FontWeight.ExtraBold),
    Font(R.font.inter_extra_light, FontWeight.ExtraLight),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_thin, FontWeight.Thin),
)

data class AppTypography(
    val headline1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 40.sp,
        lineHeight = 52.sp
    ),
    val headline2: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp,
        lineHeight = 41.6.sp
    ),
    val headline3: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    val headline4: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 20.8.sp
    ),
    val title1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp,
        lineHeight = 23.4.sp
    ),
    val title2: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 20.8.sp
    ),
    val title3: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 18.2.sp
    ),
    val subtitle1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
        lineHeight = 23.4.sp
    ),
    val subtitle2: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 20.8.sp
    ),
    val subtitle3: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 18.2.sp
    ),
    val body1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 20.8.sp
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 18.2.sp
    ),
    val body3: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 15.6.sp
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 12.sp,
        lineHeight = 15.6.sp
    ),
    val overline1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 10.sp,
        lineHeight = 13.sp
    ),
    val overline2: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 10.sp,
        lineHeight = 13.sp
    ),

    val fontFamily: FontFamily = Inter,
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
