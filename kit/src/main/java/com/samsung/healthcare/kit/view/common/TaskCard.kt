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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun TaskCard(
    @DrawableRes id: Int = R.drawable.ic_task,
    taskName: String,
    description: String,
    buttonText: String? = null,
    buttonEnabled: Boolean = true,
    onClick: () -> Unit = { },
) {
    val shape = RoundedCornerShape(4.dp)

    if (buttonEnabled)
        Card(
            shape = shape,
            backgroundColor = AppTheme.colors.surface,
            modifier = Modifier
                .fillMaxWidth()
                .height(151.dp)
                .shadow(elevation = 2.dp, shape = shape, clip = false),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id),
                            contentDescription = "",
                            modifier = Modifier
                                .width(56.dp)
                                .height(56.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = taskName,
                            style = AppTheme.typography.subHeader2,
                            color = AppTheme.colors.textPrimary,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = description,
                            style = AppTheme.typography.body3,
                            color = AppTheme.colors.textHint,
                            lineHeight = 15.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                val taskButtonName = buttonText ?: LocalContext.current.getString(R.string.start_task)
                RoundButton(taskButtonName, modifier = Modifier.fillMaxWidth(1f)) {
                    onClick()
                }
            }
        }
    else
        Card(
            shape = shape,
            backgroundColor = AppTheme.colors.surface,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(elevation = 2.dp, shape = shape, clip = false),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "",
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp),
                        tint = AppTheme.colors.textHint
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = taskName,
                            style = AppTheme.typography.subHeader2,
                            color = AppTheme.colors.textHint,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = description,
                            style = AppTheme.typography.body3,
                            color = AppTheme.colors.textHint,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() =
    TaskCard(
        taskName = "Medical History Survey",
        description = "Please fill out this survey and help us get to know your health condition",
        buttonText = "Get started"
    ) { }

@Preview(showBackground = true)
@Composable
fun TaskCardCompletedPreview() =
    TaskCard(
        taskName = "Medical History Survey",
        description = "Please fill out this survey and help us get to know your health condition",
        buttonText = "Get started",
        buttonEnabled = false
    ) { }
