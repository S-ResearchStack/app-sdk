package healthstack.kit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme

@Composable
fun ExerciseCard(
    title: String,
    values: List<String>,
) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            style = AppTheme.typography.body2,
            color = AppTheme.colors.onBackground,
            text = title,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth().height(16.dp),
        ) {
            values.forEachIndexed { index, value ->
                Text(
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.onBackground,
                    text = value,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.wrapContentWidth().padding(end = 4.dp)
                )
                if (index != values.size - 1) {
                    Divider(
                        color = AppTheme.colors.onBackground.copy(0.12F),
                        modifier = Modifier.fillMaxHeight().width(1.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                }
            }
        }
    }
}
