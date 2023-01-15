package healthstack.kit.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import healthstack.kit.R

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
    var appTitle: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    val title1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 40.sp
    ),
    val title2: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    val title3: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),

    val subHeader1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp
    ),

    val subHeader2: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),

    val topBar: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp
    ),

    val body1: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),

    val body2: TextStyle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),

    val body3: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    ),

    val body4: TextStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 10.sp
    ),
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
