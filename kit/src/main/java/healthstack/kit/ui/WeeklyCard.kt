package healthstack.kit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeeklyCard(
    targetDay: LocalDate,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(71.dp),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val today = targetDay
        val modifier = Modifier
            .width(46.dp)

        PreviousDays(today, modifier)
        Today(today, modifier)
        NextDays(today, modifier)
    }
}

@Composable
private fun NextDays(today: LocalDate, modifier: Modifier) {
    val x = if (today.dayOfWeek == DayOfWeek.SUNDAY) 0 else today.dayOfWeek.value
    for (d in 1 until (7 - x)) {
        DayCard(today.plusDays(d.toLong()), AppTheme.colors.primary.copy(0.38F), modifier)
    }
}

@Composable
private fun Today(today: LocalDate, modifier: Modifier) {
    DayCard(
        today,
        AppTheme.colors.primary,
        modifier.background(
            color = AppTheme.colors.primary.copy(0.1F),
            shape = RoundedCornerShape(2.dp),
        ),
        selected = true
    )
}

@Composable
private fun PreviousDays(today: LocalDate, modifier: Modifier) {
    if (today.dayOfWeek == DayOfWeek.SUNDAY) return
    for (d in (today.dayOfWeek.value) downTo 1) {
        DayCard(today.minusDays(d.toLong()), AppTheme.colors.disabled, modifier)
    }
}

@Composable
private fun DayCard(date: LocalDate, textColor: Color, modifier: Modifier, selected: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
            style = AppTheme.typography.caption,
            modifier = Modifier.height(16.dp),
            color = textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            date.dayOfMonth.toString(),
            style = AppTheme.typography.headline3,
            modifier = Modifier.height(26.dp),
            color = textColor
        )
        Spacer(modifier = Modifier.height(5.dp))
        if (selected) Divider(
            color = AppTheme.colors.primary,
            thickness = 2.dp
        )
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun WeeklyCardPreview() =
    WeeklyCard(LocalDate.now())
