package healthstack.kit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun PlainText(
    header: String,
    body: String,
    article: (@Composable () -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = header,
            style = AppTheme.typography.headline4,
            color = AppTheme.colors.onSurface,
        )

        if (article != null)
            Box(
                Modifier.padding(vertical = 16.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                article()
            }

        Text(
            modifier = Modifier.padding(top = 16.dp)
                .fillMaxWidth(),
            text = body,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.onSurface,
        )
    }
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun PlainTextPreview() =
    PlainText(
        "Study Explanation",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et " +
            "dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
            " aliquip ex ea commodo consequat. \n" +
            "\n" +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse " +
            "cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa " +
            "qui officia deserunt mollit anim id est laborum.\n" +
            "\n" +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse " +
            "cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa " +
            "qui officia deserunt mollit anim id est laborum."
    ) {
        Column {
            DialCard(
                R.drawable.ic_call,
                "Central Line",
                "9257643619",
                "9257643619"
            )
            Spacer(Modifier.height(10.dp))
            DialCard(
                R.drawable.ic_call,
                "Study Operator",
                "9257643619",
                "9257643619"
            )
        }
    }

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun PlainText2Preview() =
    PlainText(
        "Study Explanation",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et " +
            "dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
            " aliquip ex ea commodo consequat. \n" +
            "\n" +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse " +
            "cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa " +
            "qui officia deserunt mollit anim id est laborum.\n" +
            "\n" +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse " +
            "cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa " +
            "qui officia deserunt mollit anim id est laborum."
    )
