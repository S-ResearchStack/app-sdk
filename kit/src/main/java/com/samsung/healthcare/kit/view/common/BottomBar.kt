package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.common.ButtonShape.ROUND
import com.samsung.healthcare.kit.view.common.ButtonShape.SQUARE

@Composable
fun BottomBar(
    color: Color = Color(0x970347F4),
    onClickDisagree: () -> Unit,
    onClickAgree: () -> Unit,
) {
    BottomAppBar(
        backgroundColor = Color(0xFFEFF5FA),
        elevation = 5.dp,
    ) {
        Text(
            text = "Disagree",
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable { onClickDisagree.invoke() }
        )
        Spacer(modifier = Modifier.weight(1f, true))
        Text(
            text = "Agree",
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable { onClickAgree.invoke() }
        )
    }
}

@Composable
fun BottomBar(
    leftButtonText: String,
    rightButtonText: String,
    onClickLeftButton: () -> Unit,
    onClickRightButton: () -> Unit,
    leftButtonEnabled: Boolean = true,
    rightButtonEnabled: Boolean = true,
) {

    BottomAppBar(
        backgroundColor = MaterialTheme.colors.background
    ) {
        BottomBarText(leftButtonText, leftButtonEnabled, onClickLeftButton)
        Spacer(modifier = Modifier.weight(1f, true))
        BottomBarText(rightButtonText, rightButtonEnabled, onClickRightButton)
    }
}

@Composable
fun BottomBar(
    text: String,
    buttonEnabled: Boolean = true,
    shape: ButtonShape = SQUARE,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if (shape == SQUARE)
            SquareButton(
                text = text,
                enabled = buttonEnabled,
            ) {
                onClick()
            }
        else if (shape == ROUND)
            RoundButton(
                text = text,
                enabled = buttonEnabled,
            ) {
                onClick()
            }
    }
}

@Composable
fun BottomBarWithGradientBackground(
    text: String,
    buttonEnabled: Boolean = true,
    gradientBrush: Brush = Brush.verticalGradient(
        0.0f to Color(0x00000000),
        0.5f to AppTheme.colors.background,
        startY = 0.0f,
        endY = 100.0f
    ),
    shape: ButtonShape = SQUARE,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(gradientBrush),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(
                    gradientBrush,
                ),
        ) {
            if (shape == SQUARE)
                SquareButton(
                    text = text,
                    enabled = buttonEnabled,
                ) {
                    onClick()
                }
            else if (shape == ROUND)
                RoundButton(
                    text = text,
                    enabled = buttonEnabled,
                ) {
                    onClick()
                }
        }
    }
}

@Composable
private fun BottomBarText(
    text: String,
    enabled: Boolean,
    onClickButton: () -> Unit,
) {
    Text(
        text = text,
        color = MaterialTheme.colors.primary,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .alpha(if (enabled) 1f else ContentAlpha.disabled)
            .clickable(enabled = enabled) {
                onClickButton()
            }
    )
}
