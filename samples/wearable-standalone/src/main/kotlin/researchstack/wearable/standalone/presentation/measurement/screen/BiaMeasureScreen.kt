package researchstack.wearable.standalone.presentation.measurement.screen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch
import researchstack.domain.model.Gender
import researchstack.domain.model.UserProfile
import researchstack.domain.model.isValid
import researchstack.domain.model.priv.FailStatusBIA
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.component.AppButton
import researchstack.wearable.standalone.presentation.component.AskFloat
import researchstack.wearable.standalone.presentation.component.AskGender
import researchstack.wearable.standalone.presentation.component.AskMeasurementUnit
import researchstack.wearable.standalone.presentation.component.AskYearBirth
import researchstack.wearable.standalone.presentation.component.OneLabel
import researchstack.wearable.standalone.presentation.component.PaginationDot
import researchstack.wearable.standalone.presentation.component.TwoLabel
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel.Completed
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel.Fail
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel.Help
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel.Measuring
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel.None
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel.RequestProfile
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.cmToFt
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.ftToCm
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.getOrientation
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.kgToLbs
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.lbsToKg
import researchstack.wearable.standalone.presentation.theme.Blue100
import researchstack.wearable.standalone.presentation.theme.ItemHomeColor
import researchstack.wearable.standalone.presentation.theme.Teal300
import researchstack.wearable.standalone.presentation.theme.TitleGray
import researchstack.wearable.standalone.presentation.theme.Typography
import researchstack.wearable.standalone.presentation.theme.UnitColor
import java.util.Calendar

enum class HelpPage {
    MOVE_WATCH_HIGHER,
    PLACE_FINGERS,
    TOUCH_ONLY,
    RAISE_ARM,
    START_MEASURE
}

enum class AskProfilePage {
    MEASUREMENT_UNIT,
    GENDER,
    AGE,
    HEIGHT,
    WEIGHT;

    fun getOrder(requestProfile: RequestProfile): Int = requestProfile.listUnsetPage.indexOf(this)

    @Composable
    fun getTextResource(requestProfile: RequestProfile): Int =
        when (requestProfile.listUnsetPage.size == 1) {
            true -> R.string.ok
            else -> R.string.next
        }
}

@Composable
fun BiaMeasureScreen(
    navController: NavHostController,
    biaMeasureViewModel: BiaMeasureViewModel = hiltViewModel()
) {
    when (biaMeasureViewModel.measureState.observeAsState(Help).value) {
        None -> BiaInitialState()
        is RequestProfile ->
            AskProfile(biaMeasureViewModel, isTracking = true) {
                biaMeasureViewModel.postMeasureState(Help)
            }

        Measuring -> BiaMeasuring(biaMeasureViewModel)
        Help -> BiaHelpView(biaMeasureViewModel)
        Fail -> BiaMeasureFail(biaMeasureViewModel, navController)
        Completed -> BiaCompleted(biaMeasureViewModel, navController)
    }

    val currentView = LocalView.current
    DisposableEffect(null) {
        currentView.keepScreenOn = true
        onDispose {
            biaMeasureViewModel.stopTracking()
            currentView.keepScreenOn = false
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BiaHelpView(biaMeasureViewModel: BiaMeasureViewModel) {
    val pagerState = rememberPagerState(pageCount = { HelpPage.values().size })
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 8.dp
        ) { page: Int ->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                BiaHelpPage(page, biaMeasureViewModel)
            }
        }
    }
    PaginationDot(pagerState = pagerState)
}

@Composable
fun BiaHelpPage(page: Int, biaMeasureViewModel: BiaMeasureViewModel) {
    var drawable by remember { mutableStateOf(R.drawable.bia_try_again_01) }
    var string by remember { mutableStateOf(R.string.bia_help_message1) }
    when (page) {
        HelpPage.MOVE_WATCH_HIGHER.ordinal -> {
            drawable = R.drawable.bia_try_again_01
            string = R.string.bia_help_message1
        }

        HelpPage.PLACE_FINGERS.ordinal -> {
            drawable = R.drawable.bia_try_again_02
            string = R.string.bia_help_message2
        }

        HelpPage.TOUCH_ONLY.ordinal -> {
            drawable = R.drawable.bia_try_again_03
            string = R.string.bia_help_message3
        }

        HelpPage.RAISE_ARM.ordinal -> {
            drawable = R.drawable.bia_try_again_04
            string = R.string.bia_help_message4
        }

        HelpPage.START_MEASURE.ordinal -> {
            string = R.string.bia_help_message5
        }
    }
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (page < HelpPage.values().size - 1) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = stringResource(id = string),
                textAlign = TextAlign.Center,
                style = Typography.body1
            )
        } else {
            Spacer(Modifier.height(100.dp))
            Text(
                text = stringResource(id = string),
                textAlign = TextAlign.Center,
                style = Typography.body1
            )
            Spacer(Modifier.height(64.dp))
            AppButton(Blue100, stringResource(id = R.string.ok)) {
                biaMeasureViewModel.startTracking()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AskProfile(
    biaMeasureViewModel: BiaMeasureViewModel,
    requestProfile_: RequestProfile = RequestProfile(listOf(AskProfilePage.WEIGHT)),
    isTracking: Boolean = false,
    content: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var requestProfile: RequestProfile = requestProfile_
    if (!isTracking) {
        biaMeasureViewModel.postMeasureState(requestProfile_)
    } else {
        if (!biaMeasureViewModel.profile.isValid()) {
            if (biaMeasureViewModel.measureState.value is RequestProfile) {
                requestProfile = biaMeasureViewModel.measureState.value as RequestProfile
            }
        }
    }

    biaMeasureViewModel.isMetric.observeAsState(true).value.let { isMetric ->
        var isMetric_ by remember { mutableStateOf(-1) }
        var height by remember { mutableStateOf(-1f) }
        var weight by remember { mutableStateOf(-1f) }
        var yearBirth by remember { mutableStateOf(-1) }
        var gender by remember { mutableStateOf(Gender.UNKNOWN) }

        biaMeasureViewModel.profile?.let {
            gender = it.gender
            height = it.height
            weight = it.weight
            yearBirth = it.yearBirth
            isMetric_ = it.isMetricUnit.toInt()
        }

        fun setProfile(page: AskProfilePage) {
            var userProfile = UserProfile(height, weight, yearBirth, gender, isMetric_.toIsMetric())

            biaMeasureViewModel.setUserProfile(userProfile)

            when (requestProfile.listUnsetPage.last() == page) {
                true -> {
                    if (!userProfile.isValid() && isTracking) {
                        userProfile = UserProfile(
                            height.toDefaultHeight(true),
                            weight.toDefaultWeight(true),
                            yearBirth.toDefaultBirthYear(),
                            gender.toDefaultGender(),
                            isMetric_.toIsMetric()
                        )
                        biaMeasureViewModel.setUserProfile(userProfile)
                    }
                    content()
                }

                else -> {}
            }
        }

        val pagerState = rememberPagerState(pageCount = { requestProfile.listUnsetPage.size })

        HorizontalPager(
            state = pagerState,
            pageSpacing = 8.dp
        ) { page: Int ->
            when (page) {
                AskProfilePage.MEASUREMENT_UNIT.getOrder(requestProfile) -> {
                    AskMeasurementUnit(
                        isMetric,
                        AskProfilePage.MEASUREMENT_UNIT.getTextResource(
                            requestProfile = requestProfile
                        )
                    ) {
                        coroutineScope.launch {
                            biaMeasureViewModel.setDefaultUnit(it)
                            isMetric_ = it.toInt()
                            setProfile(AskProfilePage.MEASUREMENT_UNIT)
                            pagerState.scrollToPage(page + 1)
                        }
                    }
                }

                AskProfilePage.GENDER.getOrder(requestProfile) -> {
                    AskGender(
                        gender.toDefaultGender(),
                        buttonLabel = AskProfilePage.GENDER.getTextResource(requestProfile)
                    ) {
                        gender = it
                        setProfile(AskProfilePage.GENDER)
                        coroutineScope.launch {
                            pagerState.scrollToPage(page + 1)
                        }
                    }
                }

                AskProfilePage.HEIGHT.getOrder(requestProfile) -> {
                    val label =
                        if (isMetric)
                            OneLabel(stringResource(id = R.string.bia_metric_height_unit))
                        else
                            TwoLabel(
                                stringResource(id = R.string.bia_imperial_ft_unit),
                                stringResource(id = R.string.bia_imperial_in_unit)
                            )

                    AskFloat(
                        stringResource(id = R.string.bia_set_height),
                        label,
                        100f.cmToFt(isMetric).toInt()..300f.cmToFt(isMetric).toInt(),
                        defaultValue = height.toDefaultHeight(isMetric),
                        AskProfilePage.HEIGHT.getTextResource(requestProfile)
                    ) {
                        height = it.ftToCm(isMetric)
                        setProfile(AskProfilePage.HEIGHT)
                        coroutineScope.launch {
                            pagerState.scrollToPage(page + 1)
                        }
                    }
                }

                AskProfilePage.WEIGHT.getOrder(requestProfile) -> {
                    val unitStringId =
                        if (isMetric) R.string.bia_metric_weight_unit else R.string.bia_imperial_weight_unit

                    AskFloat(
                        stringResource(id = R.string.bia_set_weight),
                        OneLabel(stringResource(id = unitStringId)),
                        0f.kgToLbs(isMetric).toInt()..200f.kgToLbs(isMetric).toInt(),
                        defaultValue = weight.toDefaultWeight(isMetric),
                        buttonLabel = R.string.ok,
                    ) {
                        weight = it.lbsToKg(isMetric)
                        setProfile(AskProfilePage.WEIGHT)
                        biaMeasureViewModel.postMeasureState(Help)
                    }
                }

                AskProfilePage.AGE.getOrder(requestProfile) -> {
                    AskYearBirth(
                        defaultValue = yearBirth.toDefaultBirthYear(),
                        buttonLabel = AskProfilePage.AGE.getTextResource(requestProfile)
                    ) {
                        yearBirth = it
                        setProfile(AskProfilePage.AGE)
                        coroutineScope.launch {
                            pagerState.scrollToPage(page + 1)
                        }
                    }
                }
            }
        }
    }
}

fun Float.toDefaultHeight(isMetric: Boolean): Float =
    if (this <= 0f) 175f.cmToFt(isMetric)
    else this.cmToFt(isMetric)

fun Float.toDefaultWeight(isMetric: Boolean): Float =
    if (this <= 0f) 75f.kgToLbs(isMetric)
    else this.kgToLbs(isMetric)

fun Gender.toDefaultGender(): Gender = when (this) {
    Gender.UNKNOWN -> Gender.FEMALE
    else -> this
}

fun Int.toDefaultBirthYear() =
    if (this <= 0) Calendar.getInstance()
        .get(Calendar.YEAR) - 20
    else this

fun Int.toIsMetric(): Boolean? = when (this) {
    1 -> true
    0 -> false
    else -> null
}

fun Boolean?.toInt() = when (this) {
    null -> -1
    true -> 1
    else -> 0
}

@Composable
fun BiaMeasureFail(biaMeasureViewModel: BiaMeasureViewModel, navController: NavHostController) {
    biaMeasureViewModel.failStatus.observeAsState().value?.let {
        Column(
            Modifier
                .fillMaxSize()
                .padding(0.dp, 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.body_composition),
                color = TitleGray,
                style = Typography.body1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = getFailMessage(it),
                style = Typography.body1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppButton(bgColor = ItemHomeColor, stringResource(id = R.string.ok)) {
                navController.navigate(Route.Main.name) {
                    popUpTo(Route.Main.name) { inclusive = true }
                }
            }
        }
    }
}

@Composable
private fun getFailMessage(failStatus: Int): String = when (failStatus) {
    FailStatusBIA.SENSOR_ERROR.status -> stringResource(id = R.string.bia_fail_message_problem_with_sensors)
    FailStatusBIA.WRIST_DETACHED.status, FailStatusBIA.WRIST_LOOSE.status -> stringResource(id = R.string.bia_fail_message_wrist_is_detached)
    FailStatusBIA.FINGER_ON_HOME_BUTTON_BROKEN.status, FailStatusBIA.FINGER_ON_BACK_BUTTON_BROKEN.status,
    FailStatusBIA.ALL_FINGER_BROKEN.status -> stringResource(id = R.string.bia_fail_message_finger_detached)

    FailStatusBIA.DRY_FINGER.status -> stringResource(id = R.string.bia_fail_message_dry_finger)
    FailStatusBIA.BODY_TOO_BIG.status -> stringResource(id = R.string.bia_fail_message_body_too_big)
    FailStatusBIA.TWO_HAND_TOUCHED_EACH_OTHER.status -> stringResource(id = R.string.bia_fail_message_two_hand_touched_each_other)
    FailStatusBIA.ALL_FINGER_CONTACT_SUS_FRAME.status -> stringResource(id = R.string.bia_fail_message_all_finger_contact_the_SUS_frame)
    FailStatusBIA.UNSTABLE_IMPEDANCE.status -> stringResource(id = R.string.bia_fail_message_unstable_impedance)
    FailStatusBIA.BODY_TOO_FAT.status -> stringResource(id = R.string.bia_fail_message_body_fat_ratio_is_outage)
    else -> "Unknown error"
}

@Composable
fun BiaCompleted(biaMeasureViewModel: BiaMeasureViewModel, navController: NavHostController) {
    biaMeasureViewModel.stopTracking()
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.body_composition),
            color = TitleGray,
            style = Typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .background(ItemHomeColor, RoundedCornerShape(15))
        ) {
            biaMeasureViewModel.result.observeAsState().value.let {
                var cnt = 0
                it?.entries?.forEach { map ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp, 4.dp)
                    ) {
                        Text(
                            text = stringResource(id = map.key),
                            color = UnitColor,
                            style = Typography.caption3
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = map.value, fontWeight = FontWeight.SemiBold)
                    }
                    if (++cnt < it.size) {
                        Divider(Modifier.padding(16.dp, 0.dp), color = Color.DarkGray)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        AppButton(bgColor = ItemHomeColor, stringResource(id = R.string.ok)) {
            navController.navigate(Route.Main.name) {
                popUpTo(Route.Main.name) { inclusive = true }
            }
        }
    }
}

@Composable
fun BiaMeasuring(biaViewModel: BiaMeasureViewModel) {
    biaViewModel.progress.observeAsState().value?.let {
        CircularProgressIndicator(
            progress = it.toFloat() / 100,
            Modifier.fillMaxSize(),
            indicatorColor = Teal300
        )
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = it, style = Typography.title1)
                Text(text = "%", modifier = Modifier.padding(bottom = 3.dp))
            }
            Box(Modifier.height(120.dp), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.health_body_composition),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
            }

            Text(text = stringResource(id = R.string.measuring))
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BiaInitialState() {
    val screenOrientation = getOrientation(LocalContext.current)
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        val bitmapLeft =
            ContextCompat.getDrawable(LocalContext.current, R.drawable.arrow_left_key)?.toBitmap()
                ?.asImageBitmap()
        val bitmapLeft1 =
            ContextCompat.getDrawable(LocalContext.current, R.drawable.arrow_left_key_1)?.toBitmap()
                ?.asImageBitmap()
        val bitmapRight =
            ContextCompat.getDrawable(LocalContext.current, R.drawable.arrow_right_key)?.toBitmap()
                ?.asImageBitmap()
        val bitmapRight1 =
            ContextCompat.getDrawable(LocalContext.current, R.drawable.arrow_right_key_1)
                ?.toBitmap()
                ?.asImageBitmap()

        Box(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    onDrawBehind {
                        if (screenOrientation == Configuration.ORIENTATION_UNDEFINED) {
                            bitmapRight?.let {
                                drawImage(
                                    it,
                                    topLeft = Offset(size.width / 2 + 144f, size.height / 2 - 144f)
                                )
                            }
                            bitmapLeft1?.let {
                                drawImage(
                                    it,
                                    topLeft = Offset(size.width / 2 + 144f, size.height / 2 + 50f)
                                )
                            }
                        } else {
                            bitmapRight1?.let {
                                drawImage(
                                    it,
                                    topLeft = Offset(16f, size.height / 2 - 144f)
                                )
                            }
                            bitmapLeft?.let {
                                drawImage(
                                    bitmapLeft,
                                    topLeft = Offset(16f, size.height / 2 + 50f)
                                )
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.bia_help_fingers),
                textAlign = TextAlign.Center
            )
        }
    }
}
