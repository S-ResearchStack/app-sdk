package com.samsung.healthcare.kit.view.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun TaskCard(
    @DrawableRes id: Int,
    taskName: String,
    description: String,
    buttonText: String? = null,
    buttonEnabled: Boolean = true,
    onClick: () -> Unit = { }
) {
    val shape = RoundedCornerShape(16.dp)
    Card(
        shape = shape,
        backgroundColor = AppTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth(1f)
            .shadow(elevation = 10.dp, shape = shape, clip = false)
        //     .clip(shape)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)
        ) {

            Row {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id),
                        contentDescription = "",
                        modifier = Modifier
                            .width(20.dp)
                            .height(23.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
                Spacer(modifier = Modifier.width(9.dp))
                Column {
                    Text(
                        text = taskName,
                        style = AppTheme.typography.title3,
                        color = AppTheme.colors.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = description,
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.textHint
                    )
                }
            }

            if (buttonEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                val taskButtonName = buttonText ?: LocalContext.current.getString(R.string.start_task)
                RoundButton(taskButtonName, modifier = Modifier.fillMaxWidth(1f)) {
                    onClick()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskCard(
        R.drawable.ic_sleep,
        "Daily mood check-in",
        "Please complete the daily check-in and jot down your thoughts for the day."
    ) {
    }
}
