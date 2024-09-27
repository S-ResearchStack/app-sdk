package researchstack.presentation.screen.insight

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import researchstack.BuildConfig
import researchstack.R
import researchstack.data.datasource.local.pref.dataStore
import researchstack.domain.model.Study
import researchstack.domain.model.StudyCategory
import researchstack.domain.model.StudyStatusModel
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.component.TopBar
import researchstack.presentation.initiate.route.Route
import researchstack.presentation.pref.UIPreference
import researchstack.presentation.screen.main.BottomPager
import researchstack.presentation.screen.study.geStudyPainter
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.descriptionColor
import researchstack.presentation.viewmodel.DeregisterViewModel
import researchstack.presentation.viewmodel.HealthConnectPermissionViewModel
import researchstack.presentation.viewmodel.SignOutViewModel
import researchstack.presentation.viewmodel.SyncHealthDataViewModel
import researchstack.presentation.viewmodel.study.InsightPasswordViewModel
import researchstack.presentation.viewmodel.study.StudyListViewModel
import researchstack.presentation.worker.WorkerRegistrar.registerDataSyncWorker
import java.time.Instant

@Composable
fun InsightScreen(
    currentPager: Int,
    insightPasswordViewModel: InsightPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var logPageEnableButtonCount = 0
    var showError by remember { mutableStateOf(false) }
    var adminPass by remember { mutableStateOf("") }

    val isSecured = BuildConfig.ADMIN_PASSWORD != ""
    val isAuthentic = insightPasswordViewModel.isAuthentic.observeAsState(initial = !isSecured).value

    LaunchedEffect(key1 = currentPager) {
        if (currentPager != BottomPager.Data.ordinal && isSecured) {
            insightPasswordViewModel.setAuthenticState(false)
        }
        showError = false
        adminPass = ""
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(1f),
        topBar = {
            TopBar(
                title = "My Data",
            ) {
                if (isAuthentic) {
                    IconButton(
                        modifier = Modifier.width(30.dp),
                        onClick = {
                            // Hidden enable
                            logPageEnableButtonCount += 1
                            if (logPageEnableButtonCount == 10) {
                                logPageEnableButtonCount = 0
                                coroutineScope.launch {
                                    UIPreference(context.dataStore).toggleLogPage()
                                }
                            }
                        }
                    ) { }
                    Spacer(modifier = Modifier.width(50.dp))
                    MoreActionMenu()
                }
            }
        },
    ) {
        if (isAuthentic) {
            InsightContent(modifier = Modifier.padding(it))
        } else {
            EnterAdminPassword(
                modifier = Modifier.padding(it),
                adminPass = adminPass,
                showError = showError,
                onClick = {
                    if (adminPass == BuildConfig.ADMIN_PASSWORD) {
                        insightPasswordViewModel.setAuthenticState(true)
                    } else {
                        showError = true
                    }
                },
                onValueChange = { value ->
                    adminPass = value
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EnterAdminPassword(
    modifier: Modifier = Modifier,
    adminPass: String,
    showError: Boolean,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .wrapContentHeight(),
            text = stringResource(id = R.string.enter_admin_password_message),
            style = AppTheme.typography.body2,
            color = descriptionColor,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(150.dp))
        TextField(
            value = adminPass,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.primary,
                    shape = RoundedCornerShape(size = 4.dp)
                ),
            placeholder = { Text(text = stringResource(id = R.string.enter_admin_password)) },
            singleLine = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                textColor = AppTheme.colors.primary,
                backgroundColor = Color(@Suppress("MagicNumber") 0xFFF6F6F6),
            ),
            textStyle = AppTheme.typography.body1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
        if (showError) {
            Text(
                text = stringResource(id = R.string.enter_admin_password_error_message),
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .padding(horizontal = 40.dp)
                .fillMaxSize(),
        ) {
            AppTextButton(stringResource(R.string.enter_data_tab_button)) {
                onClick()
            }
        }
    }
}

@Composable
fun InsightContent(
    modifier: Modifier = Modifier,
    studyListViewModel: StudyListViewModel = hiltViewModel(),
    healthConnectPermissionViewModel: HealthConnectPermissionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val healthConnectPermissionLauncher =
        rememberLauncherForActivityResult(healthConnectPermissionViewModel.requestPermissionActivityContract()) {}
    LaunchedEffect(null) {
        studyListViewModel.getMyStudy()
    }
    val myActiveStudies = studyListViewModel.myActiveStudies.collectAsState().value
    val myCompletedStudies = studyListViewModel.myCompletedStudies.collectAsState().value
    val showHealthConnectPermissionToast by healthConnectPermissionViewModel.showToast.collectAsState()
    if (showHealthConnectPermissionToast) {
        Toast.makeText(context, "All permissions are already enabled", Toast.LENGTH_SHORT).show()
        healthConnectPermissionViewModel.onToastShown()
    }

    Column(
        modifier = modifier
            .padding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .fillMaxSize()
    ) {
       AppTextButton(
            text = "Check Health Connect access",
            backgroundColor = AppTheme.colors.dataVisualization2.copy(@Suppress("MagicNumber") 0.8f),
            borderColor = AppTheme.colors.dataVisualization2,
        ) {
            healthConnectPermissionViewModel.requestPermission(
                healthConnectPermissionLauncher
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        MyStudyList(
            title = stringResource(id = R.string.registered_study_title),
            studies = myActiveStudies,
            StudyCategory.REGISTERED
        )
        if (BuildConfig.SHOW_COMPLETE_STUDY) {
            MyStudyList(
                title = stringResource(id = R.string.completed_study_title),
                studies = myCompletedStudies,
                StudyCategory.COMPLETED
            )
        }
    }
}

@Composable
fun MyStudyList(
    title: String,
    studies: List<Study>,
    studyCategory: StudyCategory,
) {
    val navController = LocalNavController.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .clickable {
                if (studyCategory == StudyCategory.REGISTERED) {
                    navController.navigate("${Route.StudyStatus.name}/${0}")
                } else navController.navigate("${Route.StudyStatus.name}/${1}")
            }
    ) {
        Text(
            text = title,
            style = AppTheme.typography.title1,
            color = AppTheme.colors.onSurface,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "ArrowForwardIos",
            tint = AppTheme.colors.onSurface,
            modifier = Modifier.height(15.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    if (studies.isEmpty()) {
        val text = if (studyCategory == StudyCategory.REGISTERED) {
            stringResource(id = R.string.join_in_study_message)
        } else stringResource(id = R.string.no_completed_message)
        Text(
            text = text,
            style = AppTheme.typography.body3,
            color = AppTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(5.dp))
    } else MyStudyList(studies)
}

@Composable
private fun MyStudyList(studies: List<Study>) {
    var count = 0
    studies.forEach {
        if (count++ >= 3) {
            return
        }
        StudyCard(study = it)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun MoreActionMenu(
    signOutViewModel: SignOutViewModel = hiltViewModel(),
    deregisterViewModel: DeregisterViewModel = hiltViewModel(),
) {
    val isDropDownMenuExpanded = remember { mutableStateOf(false) }
    Box {
        IconButton(
            onClick = {
                isDropDownMenuExpanded.value = true
            }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "MoreVert",
                tint = AppTheme.colors.onSurface,
            )
        }
        DropdownMenu(
            expanded = isDropDownMenuExpanded.value,
            onDismissRequest = { isDropDownMenuExpanded.value = false },
        ) {
            SyncMenuItem()
            SettingMenuItem()
            if (BuildConfig.ENABLE_LOGOUT) {
                MenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    isDropDownMenuExpanded = isDropDownMenuExpanded,
                    onOk = { signOutViewModel.signOut() },
                    title = stringResource(R.string.log_out),
                    notify = stringResource(R.string.log_out_notify),
                )
                MenuItem(
                    icon = Icons.Default.PersonRemove,
                    isDropDownMenuExpanded = isDropDownMenuExpanded,
                    onOk = {
                        deregisterViewModel.deregister(
                            onComplete = {
                                signOutViewModel.signOut()
                            }
                        )
                    },
                    title = stringResource(R.string.deregister),
                    notify = stringResource(R.string.deregister_notify)
                )
            }
        }
    }
}

@Composable
private fun SettingMenuItem() {
    val navController = LocalNavController.current
    DropdownMenuItem(
        onClick = {
            navController.navigate(Route.Settings.name)
        }
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "sync",
                tint = AppTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = AnnotatedString("Setting"),
            )
        }
    }
}

@Composable
private fun SyncMenuItem() {
    val context = LocalContext.current
    // FIXME remember forever
    var lastTimestamp by remember { mutableStateOf(0L) }

    DropdownMenuItem(
        enabled = 1 * 60 < (Instant.now().epochSecond - lastTimestamp),
        onClick = {
            lastTimestamp = Instant.now().epochSecond
            registerDataSyncWorker(context)
        }
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "sync",
                tint = AppTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = AnnotatedString("Sync"),
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    isDropDownMenuExpanded: MutableState<Boolean>,
    onOk: () -> Unit,
    title: String,
    notify: String,
) {
    val showDialog = remember { mutableStateOf(false) }
    DropdownMenuItem(onClick = {
        showDialog.value = true
    }) {
        Row {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = AppTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.width(10.dp))
            if (showDialog.value) {
                AlertDialogComponent(
                    title = title,
                    dialogNotify = notify,
                    showDialog = showDialog.value,
                    onOk = {
                        onOk()
                    },
                    onDismiss = {
                        showDialog.value = false
                        isDropDownMenuExpanded.value = false
                    }
                )
            }
            Text(
                text = AnnotatedString(title),
            )
        }
    }
}

@Composable
fun AlertDialogComponent(
    title: String,
    dialogNotify: String,
    showDialog: Boolean,
    onOk: () -> Unit,
    onDismiss: () -> Unit,
) {
    val navController = LocalNavController.current
    if (showDialog) {
        AlertDialog(
            title = {
                Text(title)
            },
            text = {
                Text(text = dialogNotify)
            },
            onDismissRequest = onDismiss,
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onOk()
                    navController.popBackStack(Route.Welcome.name, false)
                    navController.navigate(Route.Welcome.name)
                }) {
                    Text(title)
                }
            },
        )
    }
}

@Composable
private fun SyncData() {
    Spacer(modifier = Modifier.height(150.dp))
    Text(
        "Sync Data",
        style = AppTheme.typography.headline3,
        color = AppTheme.colors.onSurface
    )
    Spacer(modifier = Modifier.height(5.dp))
    SyncDataButton()
}

@Composable
private fun SyncDataButton(
    syncHealthDataViewModel: SyncHealthDataViewModel = hiltViewModel(),
) {
    AppTextButton(text = "Sync Health Data") {
        syncHealthDataViewModel.syncHealthData()
    }
}

@Suppress("MagicNumber")
@Composable
fun StudyCard(study: Study) {
    val navController = LocalNavController.current
    val shape = RoundedCornerShape(4.dp)

    Card(
        shape = shape,
        backgroundColor = AppTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .height(91.dp)
            .clickable { navController.navigate("${Route.StudyPermissionSetting.name}/${study.id}") },
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .shadow(
                            elevation = 3.dp,
                            spotColor = Color(0x33000000),
                            ambientColor = Color(0x33000000)
                        )
                        .shadow(
                            elevation = 1.dp,
                            spotColor = Color(0x1F000000),
                            ambientColor = Color(0x1F000000)
                        )
                        .shadow(
                            elevation = 1.dp,
                            spotColor = Color(0x24000000),
                            ambientColor = Color(0x24000000)
                        )
                        .background(AppTheme.colors.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = study.geStudyPainter(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(56.dp)
                            .height(56.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = study.name,
                        style = AppTheme.typography.title2,
                        color = AppTheme.colors.onSurface,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = study.organization,
                        style = AppTheme.typography.body3,
                        color = descriptionColor,
                    )
                    when (study.status) {
                        StudyStatusModel.STUDY_STATUS_COMPLETE -> {
                            Text(
                                text = stringResource(R.string.completed_status),
                                style = AppTheme.typography.body3,
                                color = AppTheme.colors.dataVisualization4,
                            )
                        }

                        StudyStatusModel.STUDY_STATUS_WITHDRAW -> {
                            Text(
                                text = stringResource(R.string.withdraw_status),
                                style = AppTheme.typography.body3,
                                color = AppTheme.colors.onDisabled2,
                            )
                        }

                        StudyStatusModel.STUDY_STATUS_DROP -> {
                            Text(
                                text = stringResource(R.string.dropped_status),
                                style = AppTheme.typography.body3,
                                color = AppTheme.colors.error,
                            )
                        }

                        else -> {}
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
