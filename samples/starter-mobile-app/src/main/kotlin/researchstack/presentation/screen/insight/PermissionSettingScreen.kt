package researchstack.presentation.screen.insight

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LabelImportant
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R.drawable
import researchstack.R.string
import researchstack.domain.model.ShareAgreement
import researchstack.domain.model.Study
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.presentation.component.AppSwitch
import researchstack.presentation.component.TopBar
import researchstack.presentation.screen.study.geStudyPainter
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.descriptionColor
import researchstack.presentation.util.toStringResourceId
import researchstack.presentation.viewmodel.permission.PermissionSettingViewModel

@Composable
fun StudyPermissionSettingScreen(
    studyId: String,
    studyPermissionSettingViewModel: PermissionSettingViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        studyPermissionSettingViewModel.getStudyAndPermissions(studyId)
        studyPermissionSettingViewModel.getRemainUploadRequest(studyId)
    }
    val study = studyPermissionSettingViewModel.study.collectAsState().value
    val shareAgreements = studyPermissionSettingViewModel.shareAgreements.collectAsState().value
    val remainFileCount = studyPermissionSettingViewModel.remainFileCount.collectAsState().value

    DisposableEffect(null) {
        onDispose {
            studyPermissionSettingViewModel.sendPassiveDataStatus()
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = stringResource(id = string.study_permission_title))
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .verticalScroll(scrollState),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                painter = study?.geStudyPainter() ?: painterResource(drawable.study_image),
                contentDescription = "study image",
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                StudyInformation(study, remainFileCount)

                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(string.data_permission),
                    style = AppTheme.typography.title2,
                    color = AppTheme.colors.onSurface,
                )

                Spacer(Modifier.height(16.dp))
                DataPermission(
                    stringResource(string.samsung_health),
                    shareAgreements.filter { it.dataType is SHealthDataType },
                    studyPermissionSettingViewModel
                )

                DataPermission(
                    stringResource(string.sensor),
                    shareAgreements.filter { it.dataType is TrackerDataType },
                    studyPermissionSettingViewModel
                )

                DataPermission(
                    stringResource(string.priv_data),
                    shareAgreements.filter { it.dataType is PrivDataType },
                    studyPermissionSettingViewModel
                )

                DataPermission(
                    stringResource(string.device_stats),
                    shareAgreements.filter { it.dataType is DeviceStatDataType },
                    studyPermissionSettingViewModel
                )
            }
        }
    }
}

@Composable
private fun StudyInformation(study: Study?, remainFileCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = study?.name ?: "",
            style = AppTheme.typography.title1,
            color = AppTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.width(12.dp))
        // NOTE:
        if (remainFileCount != 0) {
            Icon(Icons.AutoMirrored.Filled.LabelImportant)
            Text(
                text = stringResource(id = string.exists_not_uploaded_files),
                style = AppTheme.typography.body3,
                color = Color.Red
            )
        }
    }
    study?.registrationId?.let { registrationId ->
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = CenterVertically,
        ) {
            Icon(Icons.Default.Info)
            Text(
                text = "Study ID: ${study.id}",
                style = AppTheme.typography.body3,
                color = descriptionColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(Icons.Default.Person)
            Text(
                text = "SubjectNumber: $registrationId",
                style = AppTheme.typography.body3,
                color = descriptionColor
            )
        }
    }
}

@Composable
private fun Icon(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = "",
        modifier = Modifier
            .size(24.dp)
            .padding(1.dp),
        tint = descriptionColor,
    )
}

@Composable
private fun DataPermission(
    category: String,
    shareAgreements: List<ShareAgreement>,
    studyPermissionSettingViewModel: PermissionSettingViewModel,
) {
    if (shareAgreements.isNotEmpty()) {
        Divider(modifier = Modifier.height(1.dp), color = AppTheme.colors.onDisabled2)
        Text(
            text = category,
            style = AppTheme.typography.subtitle2,
            color = AppTheme.colors.onSurface,
            modifier = Modifier.padding(top = 10.dp),
        )
        Spacer(Modifier.height(20.dp))
        shareAgreements.forEach { shareAgreement ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = stringResource(id = shareAgreement.dataType.toStringResourceId()),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.onSurface,
                )
                AppSwitch(
                    shareAgreement.approval,
                ) {
                    studyPermissionSettingViewModel.updateShareAgreement(
                        shareAgreement.copy(approval = it)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MoreActionMenu() {
}
