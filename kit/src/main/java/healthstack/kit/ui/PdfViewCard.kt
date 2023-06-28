package healthstack.kit.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import healthstack.kit.theme.AppTheme

@Composable
fun PdfViewCard(
    type: String,
    title: String,
    buttonText: String = "View PDF",
    filePath: String,
    drawableId: Int,
) {
    Row(
        Modifier.fillMaxWidth().height(176.dp),
    ) {
        Column(Modifier.width(122.dp).height(176.dp)) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = null,
                modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                contentScale = ContentScale.FillHeight
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(
            Modifier.fillMaxWidth().height(176.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.wrapContentHeight().fillMaxWidth()) {
                    Text(
                        text = type,
                        style = AppTheme.typography.title3,
                        color = AppTheme.colors.onBackground.copy(0.6F),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.padding(2.dp))
                    Text(
                        text = title,
                        style = AppTheme.typography.headline3,
                        color = AppTheme.colors.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            }
            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = { }, // TODO: Open Pdf
                    modifier = Modifier.height(40.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, AppTheme.colors.primary),
                    contentPadding = PaddingValues(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = AppTheme.colors.background,
                        disabledContentColor = AppTheme.colors.primary,
                    )
                ) {
                    Text(
                        text = buttonText,
                        style = AppTheme.typography.title2,
                        color = AppTheme.colors.primary
                    )
                }
            }
        }
    }
}
