package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomSquareButton(
    text: String = "Dummy name",
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        SquareButton(
            text = text
        ) {
            onClick()
        }
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
            text = text
        ) {
            onClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomButtonPreview() {
    BottomSquareButton("hello") {}
}

@Preview(showBackground = true)
@Composable
fun ButtomRoundButtonPreview() {
    BottomRoundButton("hello") {}
}
