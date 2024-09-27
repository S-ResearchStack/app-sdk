package researchstack.presentation.screen.study

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R
import researchstack.R.string
import researchstack.domain.model.InformedConsent
import researchstack.domain.model.ParticipationRequirement
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.LabeledCheckbox
import researchstack.presentation.component.LoadingIndicator
import researchstack.presentation.component.TopBar
import researchstack.presentation.initiate.route.MainPage
import researchstack.presentation.initiate.route.Route
import researchstack.presentation.initiate.route.Route.Main
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.util.showMessage
import researchstack.presentation.util.toStringResourceId
import researchstack.presentation.viewmodel.study.ConsentInformViewModel
import researchstack.presentation.viewmodel.study.StudyViewModel
import researchstack.presentation.viewmodel.study.StudyViewModel.Fail
import researchstack.presentation.viewmodel.study.StudyViewModel.Joining
import researchstack.presentation.viewmodel.study.StudyViewModel.NotStarted
import researchstack.presentation.viewmodel.study.StudyViewModel.Success
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView

@Composable
fun StudyInformedConsentScreen(
    consentInformViewModel: ConsentInformViewModel = hiltViewModel(),
    studyViewModel: StudyViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val participationRequirement =
        consentInformViewModel.participationRequirement.collectAsState().value

    var page by remember { mutableStateOf<String?>(null) }
    var requestPermission by remember { mutableStateOf(false) }
    val study = studyViewModel.study.collectAsState().value
    val joinState = studyViewModel.joinState.collectAsState().value

    if (study != null && participationRequirement != null && page == null) {
        BackHandler {
            val skipEligibilityTest = participationRequirement.eligibilityTest.sections.isEmpty()

            navController.popBackStack(
                route = "${Route.StudyEligibility.name}/{studyId}",
                inclusive = skipEligibilityTest,
            )
        }

        UserAgreement(
            participationRequirement,
            informedConsent = participationRequirement.informedConsent,
            onRequestPage = { page = it }
        ) {
            studyViewModel.joinStudy(
                participationRequirement.informedConsent.studyId,
            )
        }
    }
    page?.let {
        ConsentInformWebView(it) {
            page = null
        }
    }

    when (joinState) {
        is NotStarted -> {}
        is Joining -> LoadingIndicator()
        is Success -> {
            LocalContext.current.showMessage(
                stringResource(id = string.joined_study_message).format(study?.name)
            )
            navController.navigate("${Main.name}/${MainPage.Data.ordinal}") {
                popUpTo(0)
            }
        }

        is Fail -> {
            LocalContext.current.showMessage(joinState.message)
        }

        else -> {}
    }
}

@Composable
fun UserAgreement(
    dataRequirement: ParticipationRequirement,
    informedConsent: InformedConsent,
    onRequestPage: (String) -> Unit,
    onClickNext: () -> Unit,
) {
    val consentList: Array<String> = stringArrayResource(R.array.consent_list)
    var isSigned by remember { mutableStateOf(false) }
    val checked = BooleanArray(consentList.size)
    var allChecked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(id = string.agreement)) { }
        },
        bottomBar = {
            Row(modifier = Modifier.padding(24.dp)) {
                AppTextButton(
                    text = stringResource(id = string.next),
                    enabled = isSigned and allChecked
                ) { onClickNext() }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            TermsOfService(informedConsent.imageUrl) {
                onRequestPage(it)
            }

            Spacer(Modifier.height(10.dp))
            DataListView(dataRequirement)
            Spacer(Modifier.height(10.dp))

            ConsentList(consentList) { index, check ->
                checked[index] = check
                if (checked.all { it }) allChecked = true
            }
            Spacer(Modifier.height(10.dp))

            SignaturePad { isSigned = true }
        }
    }
}

@Composable
private fun DataListView(dataRequirement: ParticipationRequirement) {
    val healthDataTypes = dataRequirement.SHealthDataTypes.map {
        it.toStringResourceId()
    }.toSet()

    if (healthDataTypes.isNotEmpty()) {
        DataListSection(string.collected_health_data_title, healthDataTypes)
        Spacer(Modifier.height(10.dp))
    }

    val sensorDataTypes = dataRequirement.trackerDataTypes.map {
        it.toStringResourceId()
    }.toSet()

    if (sensorDataTypes.isNotEmpty()) {
        DataListSection(string.collected_sensor_data_title, sensorDataTypes)
        Spacer(Modifier.height(10.dp))
    }

    val privDataTypes = dataRequirement.privDataTypes.map {
        it.toStringResourceId()
    }.toSet()
    if (privDataTypes.isNotEmpty()) {
        DataListSection(string.collected_priv_data_title, privDataTypes)
        Spacer(Modifier.height(10.dp))
    }

    val deviceStatDataTypes = dataRequirement.deviceStatDataTypes.map {
        it.toStringResourceId()
    }.toSet()
    if (deviceStatDataTypes.isNotEmpty()) {
        DataListSection(string.collected_device_stat_data_title, deviceStatDataTypes)
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun DataListSection(@StringRes titleResourceId: Int, healthDataTypes: Set<Int>) {
    Text(
        text = stringResource(id = titleResourceId),
        style = AppTheme.typography.title3,
        color = AppTheme.colors.onSurface,
    )
    healthDataTypes.chunked(2).forEach {
        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(150.dp),
                text = "- ${stringResource(id = it[0])}",
                style = AppTheme.typography.body2,
                color = AppTheme.colors.onSurface
            )
            if (it.size == 2) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "- ${stringResource(id = it[1])}",
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
private fun SignaturePad(onSignChange: (Boolean) -> Unit) {
    var signaturePadAdapter: SignaturePadAdapter? = null
    var isSigned by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(
                            maxWidth = constraints.maxWidth + 48.dp.roundToPx()
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                }
                .height(150.dp)
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Transparent)
                .background(Color(@Suppress("MagicNumber") 0xFFF6F6F6))
        ) {
            SignaturePadView(
                onReady = { signaturePadAdapter = it },
                onStartSigning = {
                    isSigned = true
                    onSignChange(true)
                },
                onClear = {
                    isSigned = false
                    onSignChange(false)
                }
            )

            if (!isSigned) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = string.write_signature),
                    color = Color.Gray,
                    style = AppTheme.typography.caption
                )
            }
        }
        ClickableText(
            buildAnnotatedString {
                val str = "Clear"
                append(str)
                addStyle(
                    style = SpanStyle(
                        color = AppTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = 0, end = str.length,
                )
            },
            modifier = Modifier.align(Alignment.End),
        ) {
            signaturePadAdapter?.clear()
        }
    }
}

@Composable
private fun ConsentList(consentList: Array<String>, onChange: (Int, Boolean) -> Unit) {
    Text(
        stringResource(id = string.inform_to_agree),
        style = AppTheme.typography.title2,
    )
    Spacer(Modifier.height(10.dp))
    consentList.forEachIndexed { index, consentMessage ->
        var isChecked by rememberSaveable { mutableStateOf(false) }
        LabeledCheckbox(
            isChecked = isChecked,
            onCheckedChange = { changed ->
                isChecked = changed
                onChange(index, changed)
            },
            labelText = consentMessage,
            style = AppTheme.typography.body2,
        )
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun TermsOfService(tosUrl: String, onClick: (String) -> Unit) {
    val tos = stringResource(id = string.study_consent)
    val message = stringResource(id = string.read_study_consent)
    val tosStartIndex = message.indexOf(tos)
    val tosEndIndex = tosStartIndex + tos.length

    Column {
        ClickableText(
            buildAnnotatedString {
                append(message)
                addStyle(
                    style = SpanStyle(
                        color = AppTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = tosStartIndex,
                    end = tosEndIndex,
                )
            },
            modifier = Modifier.align(Alignment.End),
            style = AppTheme.typography.title2
        ) { index ->
            if (index in tosStartIndex..tosEndIndex) {
                onClick(tosUrl)
            }
        }
    }
}

@Composable
private fun ConsentInformWebView(url: String, onClick: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    BackHandler(onBack = onClick)

    Scaffold(
        bottomBar = {
            Row(modifier = Modifier.padding(20.dp)) {
                AppTextButton(text = stringResource(id = string.confirm), onClick = onClick)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val webViewClient = WebViewClient()
            AndroidView(
                factory = {
                    WebView(context).apply {
                        this.webViewClient = webViewClient
                        this.loadUrl(url)
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .background(AppTheme.colors.background)
            )
        }
    }
}
