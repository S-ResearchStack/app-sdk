package healthstack.kit.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R
import healthstack.kit.R.drawable
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun StatusCard(@DrawableRes drawableId: Int, value: String, unit: String = "") {
    val shape = RoundedCornerShape(4.dp)
    Card(
        shape = shape,
        backgroundColor = AppTheme.colors.background,
        modifier = Modifier
            .wrapContentSize()
            .shadow(elevation = 2.dp, shape = shape, clip = false)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .height(150.dp)
        ) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = "",
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = AppTheme.typography.title2,
                color = AppTheme.colors.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = unit,
                style = AppTheme.typography.body3,
                color = AppTheme.colors.onSurface.copy(0.6F),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
fun TaskStatus(@DrawableRes drawableId: Int, value: String, unit: String = "") {
    Card(
        elevation = 2.dp,
        backgroundColor = AppTheme.colors.surface,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .size(152.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = value,
                style = AppTheme.typography.headline3,
                color = AppTheme.colors.onSurface,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = unit,
                style = AppTheme.typography.overline1,
                color = AppTheme.colors.onSurface,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HealthDataStatusRow(@DrawableRes drawableId: Int, value: String, unit: String = "") {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 12.5.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp),
                alignment = Alignment.Center
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .width(80.dp)
                .height(40.dp),

            ) {
            Row {
                Text(
                    text = value,
                    style = AppTheme.typography.headline4,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
            Row {
                Text(
                    text = unit,
                    style = AppTheme.typography.caption,
                    color = AppTheme.colors.onSurface.copy(0.6F),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun TaskStatusCardPreview() =
    TaskStatus(R.drawable.ic_task, "5", "TASKS\nREMAINING")

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun HealthDataStatusCardPreview() =
    Card(
        elevation = 2.dp,
        backgroundColor = AppTheme.colors.surface,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .size(152.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp
                )
        ) {
            HealthDataStatusRow(R.drawable.ic_heart, "87", "BPM")
            HealthDataStatusRow(R.drawable.ic_step, "8,735", "Steps")
        }
    }

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun StatusCardPreview() =
    Box {
        StatusCard(drawable.ic_100tb, "87", "BPM")
    }
