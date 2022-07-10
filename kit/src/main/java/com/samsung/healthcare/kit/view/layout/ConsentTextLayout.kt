package com.samsung.healthcare.kit.view.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.LocalImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.external.source.HealthPlatformAdapter
import com.samsung.healthcare.kit.model.ConsentTextModel
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.common.BottomBarWithGradientBackground
import com.samsung.healthcare.kit.view.common.LabeledCheckbox
import com.samsung.healthcare.kit.view.common.TopBar
import java.nio.ByteBuffer

@Composable
fun ConsentTextLayout(
    signature: String = "",
    model: ConsentTextModel,
    buttonText: String,
    callbackCollection: CallbackCollection,
    onClickPad: () -> Unit = {},
) {
    var allChecked by rememberSaveable { mutableStateOf(model.isAllChecked()) }
    val scrollState = rememberScrollState()
    var isEveryPermissionActive by rememberSaveable { mutableStateOf(false) }
    // TODO: what if this application is not using health data at all?
    val healthPlatformAdapter = HealthPlatformAdapter.getInstance()

    LaunchedEffect(true) {
        healthPlatformAdapter.requestPermissions()
        if (healthPlatformAdapter.hasAllPermissions())
            isEveryPermissionActive = true
    }

    Scaffold(
        topBar = {
            TopBar(title = model.title) {
                callbackCollection.prev()
            }
        },
        bottomBar = {
            BottomBarWithGradientBackground(
                text = buttonText,
                buttonEnabled = allChecked && signature.isNotBlank() && isEveryPermissionActive,
            ) {
                callbackCollection.next()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = model.subTitle,
                    style = MaterialTheme.typography.h6,
                    color = AppTheme.colors.textPrimary
                )
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = model.description,
                    style = MaterialTheme.typography.body1,
                    lineHeight = 23.sp,
                    color = AppTheme.colors.textPrimary
                )

                model.checkBoxTexts.forEachIndexed { index, consentMessage ->
                    var isChecked by rememberSaveable { mutableStateOf(model.selections[index]) }

                    LabeledCheckbox(
                        isChecked = isChecked,
                        onCheckedChange = { checked ->
                            isChecked = checked
                            model.selections[index] = checked
                            allChecked = model.isAllChecked()
                        },
                        labelText = consentMessage
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 10.dp)
                    .background(color = Color(0x33C4C4C4))
                    .clickable {
                        onClickPad()
                    }
            ) {
                if (signature.isBlank())
                    Text(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        text = "Tap to sign."
                    )
                else
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0x33C4C4C4)
                    ) {
                        val imageLoader = ImageLoader
                            .Builder(LocalContext.current)
                            .components { add(SvgDecoder.Factory()) }
                            .build()

                        CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = ByteBuffer.wrap(signature.toByteArray())
                                ),
                                contentDescription = null,
                            )
                        }
                    }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConsentTextLayoutPreview() =
    ConsentTextLayout(
        "",
        ConsentTextModel(
            "id",
            "Consent",
            "Privacy Header",
            stringResource(R.string.lorem_ipsum_short),
            listOf("I agree", "I agree to share my data.", "Some Message"),
        ),
        "Done",
        CallbackCollection()
    )
