package healthstack.kit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.TextType.BULLET
import healthstack.kit.ui.TextType.NUMBER
import healthstack.kit.ui.TextType.PARAGRAPH

enum class TextType {
    NUMBER,
    BULLET,
    PARAGRAPH,
}

@Composable
fun ListedText(
    bodies: List<String>,
    type: TextType = PARAGRAPH,
    horizontalPadding: Dp = 24.dp,
    fontStyle: TextStyle = AppTheme.typography.body1,
    fontColor: Color = AppTheme.colors.onSurface,
    textAlign: TextAlign = TextAlign.Center, // Only for the paragraph type
) = when (type) {
    NUMBER -> bodies.forEachIndexed { idx, body ->
        Row(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${idx + 1}.",
                modifier = Modifier
                    .width(20.dp),
                style = fontStyle,
                color = fontColor,
                textAlign = TextAlign.Left,
            )

            Text(
                text = body,
                modifier = Modifier
                    .wrapContentHeight(),
                style = fontStyle,
                color = fontColor,
                textAlign = TextAlign.Left,
            )
        }
    }

    BULLET -> bodies.forEach {
        Row(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "\u2022",
                modifier = Modifier
                    .width(20.dp),
                style = fontStyle,
                color = fontColor,
                textAlign = TextAlign.Left,
            )

            Text(
                text = it,
                modifier = Modifier
                    .wrapContentHeight(),
                style = fontStyle,
                color = fontColor,
                textAlign = TextAlign.Left,
            )
        }
    }

    else -> bodies.forEach {
        Text(
            text = it,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            style = fontStyle,
            color = fontColor,
            textAlign = textAlign
        )
    }
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun ListedTextBulletPreview() =
    ListedText(
        listOf(
            "Hey This is first paragraph",
            "Hey this is my second paragraph. Any this is 2nd line.",
            "Hey this is 3rd paragraph."
        ),
        BULLET
    )

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun ListedTextNumberPreview() =
    ListedText(
        listOf(
            "Hey This is first paragraph",
            "Hey this is my second paragraph. Any this is 2nd line.",
            "Hey this is 3rd paragraph."
        ),
        NUMBER
    )

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun ListedTextPreview() =
    ListedText(
        listOf(
            "Hey This is first paragraph",
            "Hey this is my second paragraph. Any this is 2nd line.",
            "Hey this is 3rd paragraph."
        )
    )
