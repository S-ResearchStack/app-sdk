package healthstack.kit.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

@Composable
fun AppTheme(
    colors: AppColors = AppTheme.colors,
    typography: AppTypography = AppTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(
        colors.background
    )

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography
    ) {
        MaterialTheme(
            colors = colors.materialColors(),
            shapes = shapes
        ) {
            content()
        }
    }
}
