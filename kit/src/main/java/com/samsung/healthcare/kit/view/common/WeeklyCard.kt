package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.theme.AppTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeeklyCard(
    targetDay: LocalDate,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(71.dp)
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
        DayCard(today.plusDays(d.toLong()), AppTheme.colors.textPrimary, modifier)
    }
}

@Composable
private fun Today(today: LocalDate, modifier: Modifier) {
    DayCard(
        today,
        AppTheme.colors.textPrimaryAccent,
        modifier.background(
            color = AppTheme.colors.primary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(20.dp)
        )
    )
}

@Composable
private fun PreviousDays(today: LocalDate, modifier: Modifier) {
    if (today.dayOfWeek == DayOfWeek.SUNDAY) return
    for (d in (today.dayOfWeek.value) downTo 1) {
        DayCard(today.minusDays(d.toLong()), Color.Gray, modifier)
    }
}

@Composable
private fun DayCard(date: LocalDate, textColor: Color, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(11.dp))
        Text(
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
            style = AppTheme.typography.body3,
            modifier = Modifier.height(16.dp),
            color = textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            date.dayOfMonth.toString(),
            style = AppTheme.typography.title2,
            fontWeight = FontWeight.W700,
            modifier = Modifier.height(28.dp),
            color = textColor
        )
        Spacer(modifier = Modifier.height(11.dp))
    }
}
