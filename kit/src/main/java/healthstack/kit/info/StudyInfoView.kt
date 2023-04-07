package healthstack.kit.info

import android.content.res.Resources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.ui.DialCard
import healthstack.kit.ui.HyperLinkAction.EMAIL
import healthstack.kit.ui.HyperLinkText
import healthstack.kit.ui.PlainText
import healthstack.kit.ui.TopBar

class StudyInfoView(
    val onClickBack: () -> Unit = {},
) {
    @Composable
    fun Render() {
        val scrollState = rememberScrollState()
        val resources: Resources = LocalContext.current.resources

        Scaffold(
            topBar = {
                TopBar("Study Information") {
                    onClickBack()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 24.dp)
                    .verticalScroll(scrollState),
            ) {
                PlainText(
                    "Study Contacts",
                    "For general queries, email"
                ) {
                    Column {
                        val contacts: Array<String> =
                            resources.getStringArray(R.array.contact_dials)

                        contacts.forEach {
                            val (name, dial) = it.split(",")

                            DialCard(
                                R.drawable.ic_call,
                                name,
                                dial,
                                dial
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }

                HyperLinkText(
                    resources.getString(R.string.contact_email),
                    resources.getString(R.string.contact_email),
                    EMAIL
                )

                Spacer(Modifier.height(54.dp))

                PlainText(
                    "Study Explanation",
                    resources.getString(R.string.study_explanation),
                )

                Spacer(Modifier.height(54.dp))

                PlainText(
                    "Reward Information",
                    resources.getString(R.string.reward_information),
                )

                Spacer(Modifier.height(54.dp))

                HyperLinkText(
                    "User Agreement",
                    resources.getString(R.string.user_agreement_url),
                )

                Spacer(Modifier.height(16.dp))

                HyperLinkText(
                    "Privacy Policy",
                    resources.getString(R.string.privacy_polity_url),
                )

                Spacer(Modifier.height(16.dp))

                HyperLinkText(
                    "Licenses",
                    resources.getString(R.string.privacy_polity_url),
                )
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 1500)
@Composable
fun StudyInfoPreview() =
    StudyInfoView().Render()
