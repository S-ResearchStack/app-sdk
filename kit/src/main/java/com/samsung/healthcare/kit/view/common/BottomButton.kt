package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun BottomButton(
    text: String = "Dummy name",
    color: Color = Color(0x970347F4),
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .wrapContentSize(align = Alignment.BottomCenter)
            .padding(vertical = 30.dp),
        border = BorderStroke(
            width = 2.dp,
            color = color,
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = color,
        )
    }
}

@Composable
fun BottomRoundButton(
    text: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        RoundButton(
            text = text,
            buttonColor = AppTheme.colors.primary,
            textColor = AppTheme.colors.background,
            modifier = Modifier
                .height(44.dp)
                .width(320.dp)
        ) {
            onClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConsentBottomButtonPreview() {
    BottomButton() {}
}
