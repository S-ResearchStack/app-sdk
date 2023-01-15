package healthstack.kit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated

@Composable
fun BottomSquareButton(
    text: String = "Dummy name",
    onClick: () -> Unit,
) {
    BottomButton(text, RoundedCornerShape(0), onClick)
}

@Composable
fun BottomRoundButton(
    text: String,
    onClick: () -> Unit,
) {
    BottomButton(text, RoundedCornerShape(50), onClick)
}

@Composable
private fun BottomButton(text: String, shape: RoundedCornerShape, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        KitButton(text = text, shape = shape) {
            onClick()
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun BottomButtonPreview() {
    BottomSquareButton("hello") {}
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ButtomRoundButtonPreview() {
    BottomRoundButton("hello") {}
}
