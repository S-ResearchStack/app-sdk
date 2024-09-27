package researchstack.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import researchstack.presentation.theme.AppTheme

private const val ALPHA = 0.6f

@Suppress("LongParameterList")
@Composable
fun RoundTextBoxWithIcon(
    label: String = "",
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "Select",
    enabled: Boolean = true,
    icon: ImageVector = Icons.Filled.AccessTimeFilled,
    onClick: () -> Unit = { },
) {
    Column(
        modifier = modifier.height(70.dp)
    ) {
        Text(
            text = label,
            style = AppTheme.typography.body3,
            maxLines = 1,
            color = AppTheme.colors.onSurface.copy(ALPHA)
        )

        Spacer(Modifier.height(6.dp))

        OutlinedButton(
            onClick = onClick,
            border = if (value.isEmpty() || !enabled)
                BorderStroke(1.dp, AppTheme.colors.primaryVariant)
            else
                BorderStroke(1.dp, AppTheme.colors.primary),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (value.isEmpty() || !enabled) AppTheme.colors.onSurface.copy(ALPHA)
                else AppTheme.colors.onSurface
            ),
            modifier = modifier.height(48.dp)
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (value.isEmpty())
                    Text(
                        text = placeholder,
                        style = AppTheme.typography.body1,
                    )
                else
                    Text(
                        text = value,
                        style = AppTheme.typography.body1,
                    )

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (enabled) AppTheme.colors.primary else AppTheme.colors.primaryVariant
                )
            }
        }
    }
}
