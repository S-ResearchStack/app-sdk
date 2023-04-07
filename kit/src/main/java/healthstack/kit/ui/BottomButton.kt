package healthstack.kit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated

@Composable
fun BottomSquareButton(
    text: String = "Dummy name",
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    BottomButton(text, RoundedCornerShape(0), enabled, onClick)
}

@Composable
fun BottomRoundButton(
    text: String = "Dummy name",
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    BottomButton(text, RoundedCornerShape(4.dp), enabled, onClick)
}

@Composable
private fun BottomButton(text: String, shape: RoundedCornerShape, enabled: Boolean = true, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp, bottom = 24.dp)
    ) {
        KitButton(text = text, shape = shape, enabled = enabled) {
            onClick()
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun BottomSquareButtonPreview() {
    BottomSquareButton("hello", onClick = nothing)
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun BottomRoundButtonPreview() {
    BottomRoundButton("hello", onClick = nothing)
}

private val nothing: () -> Unit = { }
