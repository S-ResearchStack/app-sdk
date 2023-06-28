package healthstack.kit.ui

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun DialCard(
    @DrawableRes drawableId: Int,
    title: String,
    description: String,
    dial: String,
) {
    val composableContext = LocalContext.current

    Card(
        Modifier
            .fillMaxWidth()
            .height(74.dp)
            .testTag("dial")
            .background(AppTheme.colors.background),
        shape = RoundedCornerShape(4.dp),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier.wrapContentSize()
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.title3
                )
                Text(
                    text = description,
                    style = AppTheme.typography.body3
                )
            }

            Column(
                Modifier.wrapContentSize()
                    .clickable {
                        composableContext.startActivity(
                            Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:$dial")
                            )
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(drawableId),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun IconCardPreview() = DialCard(
    R.drawable.ic_call,
    "Central Line",
    "9257643619",
    "9257643619"
)
