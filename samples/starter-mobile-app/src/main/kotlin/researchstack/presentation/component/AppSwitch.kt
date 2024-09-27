package researchstack.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import researchstack.presentation.theme.AppTheme

@Composable
fun AppSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onUpdate: (Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .width(36.dp)
            .height(20.dp)
            .clickable {
                onUpdate(!checked)
            },
        shape = RoundedCornerShape(16.dp), elevation = 0.dp
    ) {
        Box(
            modifier = Modifier.background(
                if (checked) AppTheme.colors.primary else AppTheme.colors.disabled
            ),
            contentAlignment = if (checked) Alignment.TopEnd else Alignment.TopStart
        ) {
            CheckCircle(modifier = Modifier.padding(1.dp))
        }
    }
}

@Composable
private fun CheckCircle(
    modifier: Modifier = Modifier,
) {
    Card(
        shape = CircleShape, modifier = modifier.size(17.dp), elevation = 0.dp
    ) {
        Box(modifier = Modifier.background(Color.White))
    }
}
