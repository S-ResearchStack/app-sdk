package researchstack.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import researchstack.presentation.theme.TextColor

@Composable
fun AppButton(bgColor: Color, title: String, onClick: () -> Unit) {
    Box(
        Modifier
            .background(bgColor, RoundedCornerShape(50))
            .width(80.dp)
            .height(32.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = TextColor,
            fontSize = 15.sp
        )
    }
}
