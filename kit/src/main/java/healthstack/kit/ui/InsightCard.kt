package healthstack.kit.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

data class InsightUnit(val value: String, val unit: String? = null, val color: Color? = null)

fun addCommaToInt(input: Int): String = "%,d".format(Locale.US, input)

@Composable
fun InsightCardWithProgress(
    title: String,
    current: Int,
    total: Int,
    unit: String,
    onClick: () -> Unit = { },
) {
    val progress = minOf(current.toFloat() / total, 1F)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(4.dp),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 19.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Row {
                    Text(
                        text = title,
                        color = AppTheme.colors.onBackground,
                        style = AppTheme.typography.title1,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(
                        text = "${addCommaToInt(current)} / ${addCommaToInt(total)} $unit",
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.onBackground.copy(0.6F)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    LinearProgressIndicator(
                        progress = progress,
                        color = AppTheme.colors.success,
                        backgroundColor = AppTheme.colors.onBackground.copy(0.06F),
                        modifier = Modifier
                            .height(24.dp)
                            .width(128.dp)
                            .clip(RoundedCornerShape(50.dp))
                    )
                    Text(
                        text = "${(progress * 100).toInt()} %",
                        style = AppTheme.typography.overline2,
                        color = AppTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ThinInsightCard(
    title: String,
    insightUnit: InsightUnit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(4.dp),
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp, vertical = 19.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                color = AppTheme.colors.onBackground,
                style = AppTheme.typography.title1,
            )
            Text(
                text = "${insightUnit.value} ${insightUnit.unit}",
                style = AppTheme.typography.body3,
                color = AppTheme.colors.onBackground.copy(0.6F)
            )
        }
    }
}

@Composable
fun InsightCard(
    title: String,
    lastUpdatedTime: LocalDateTime,
    insightUnits: List<InsightUnit>? = null,
    onClick: () -> Unit = { },
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(4.dp),
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 19.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = AppTheme.colors.onBackground,
                    style = AppTheme.typography.title1,
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = AppTheme.colors.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
            Row {
                Text(
                    text = "Last Updated: ${
                    lastUpdatedTime.format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.SHORT
                        )
                    )
                    }",
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.onBackground.copy(0.6F)
                )
            }
            if (insightUnits != null)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    insightUnits.forEach { insight ->
                        Text(
                            text = insight.value,
                            style = AppTheme.typography.headline1,
                            color = insight.color ?: AppTheme.colors.primary
                        )
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        insight.unit?.let {
                            Text(
                                text = it,
                                style = AppTheme.typography.subtitle3,
                                color = AppTheme.colors.onBackground.copy(0.6F)
                            )
                            Spacer(Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun InsightCardPreview() =
    InsightCard(
        title = "Exercise",
        LocalDateTime.now(),
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun InsightCardPreview2() =
    InsightCard(
        title = "Sleep",
        LocalDateTime.now(),
        listOf(InsightUnit("7", "hours"), InsightUnit("24", "minutes"))
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun InsightCardPreview3() =
    InsightCard(
        title = "Respiratory Rate",
        LocalDateTime.now(),
        listOf(InsightUnit("14.5", "bpm"))
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun InsightCardPreview4() =
    InsightCard(
        title = "Stress Level",
        LocalDateTime.now(),
        listOf(InsightUnit("High", null, Color(0xFFD14343)))
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun InsightCardWithProgressPreview() =
    InsightCardWithProgress(
        "Target Goal",
        1521,
        6000,
        "steps"
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ThinInsightCardPreview() =
    ThinInsightCard(
        title = "title",
        insightUnit = InsightUnit(
            value = "100",
            unit = "steps"
        )
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun InsightCardWithProgressPreview2() =
    InsightCardWithProgress(
        "Target Goal",
        7,
        7,
        "days"
    )
