package researchstack.presentation.main.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import researchstack.BuildConfig
import researchstack.R
import researchstack.presentation.component.AppButton
import researchstack.presentation.main.MainActivity
import researchstack.presentation.main.NoteActivity
import researchstack.presentation.main.SettingActivity
import researchstack.presentation.main.viewmodel.HomeViewModel
import researchstack.presentation.measurement.BiaActivity
import researchstack.presentation.measurement.EcgActivity
import researchstack.presentation.measurement.PpgActivity
import researchstack.presentation.measurement.SpO2Activity
import researchstack.presentation.theme.HomeScreenItemBackground
import researchstack.presentation.theme.SubTextColor
import researchstack.presentation.theme.TextColor
import researchstack.presentation.worker.SyncPrivDataWorker

enum class HomeScreenItem {
    BLOOD_OXYGEN,
    ECG,
    BODY_COMPOSITION,
    PPG_RED,
    PPG_IR
}

@Composable
fun HomeScreenItem.getItemTitle(): String {
    val context = LocalContext.current
    return when (this) {
        HomeScreenItem.BLOOD_OXYGEN -> context.getString(R.string.blood_oxygen)
        HomeScreenItem.ECG -> context.getString(R.string.ecg)
        HomeScreenItem.BODY_COMPOSITION -> context.getString(R.string.body_composition)
        HomeScreenItem.PPG_RED -> context.getString(R.string.ppg_red)
        HomeScreenItem.PPG_IR -> context.getString(R.string.ppg_ir)
    }
}

fun HomeScreenItem.getItemPrefKey(): Preferences.Key<Long> {
    return when (this) {
        HomeScreenItem.BLOOD_OXYGEN -> longPreferencesKey(HomeScreenItem.BLOOD_OXYGEN.name)
        HomeScreenItem.ECG -> longPreferencesKey(HomeScreenItem.ECG.name)
        HomeScreenItem.BODY_COMPOSITION -> longPreferencesKey(HomeScreenItem.BODY_COMPOSITION.name)
        HomeScreenItem.PPG_RED -> longPreferencesKey(HomeScreenItem.PPG_RED.name)
        HomeScreenItem.PPG_IR -> longPreferencesKey(HomeScreenItem.PPG_IR.name)
    }
}

fun HomeScreenItem.getItemIcon(): Int {
    return when (this) {
        HomeScreenItem.BLOOD_OXYGEN -> R.drawable.health_blood_oxygen
        HomeScreenItem.ECG -> R.drawable.health_ecg
        HomeScreenItem.BODY_COMPOSITION -> R.drawable.health_body_composition
        HomeScreenItem.PPG_RED -> R.drawable.ppg_red
        HomeScreenItem.PPG_IR -> R.drawable.ppg_ir
    }
}

fun HomeScreenItem.getItemActivityClass(): Class<*> {
    return when (this) {
        HomeScreenItem.BLOOD_OXYGEN -> SpO2Activity::class.java
        HomeScreenItem.ECG -> EcgActivity::class.java
        HomeScreenItem.BODY_COMPOSITION -> BiaActivity::class.java
        HomeScreenItem.PPG_RED, HomeScreenItem.PPG_IR -> PpgActivity::class.java
    }
}

@Composable
fun HomeScreenItem.View(hashLastMeasure: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(57.dp)
            .padding(top = 8.dp)
            .background(HomeScreenItemBackground, RoundedCornerShape(26))
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = painterResource(id = getItemIcon()),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = getItemTitle(),
                color = TextColor,
                fontSize = 16.sp
            )
            if (hashLastMeasure.isNotBlank()) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = hashLastMeasure,
                    color = SubTextColor,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun HomeScreen(context: Context, homeViewModel: HomeViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dimensionResource(id = R.dimen.cardview_compat_inset_shadow)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.app_name_wearable),
            color = TextColor,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.app_message),
            textAlign = TextAlign.Center,
            color = TextColor,
            fontSize = 16.sp
        )

        val homeScreenItem = HomeScreenItem.values().filter {
            homeViewModel.ecgMeasurementEnabled.observeAsState().value == true ||
                (it != HomeScreenItem.ECG && it != HomeScreenItem.BODY_COMPOSITION)
        }

        for (homeItem in homeScreenItem) {
            homeViewModel.getLiveDataByType(homeItem).observeAsState().value.let {
                homeItem.View(it ?: "") {
                    val intent = Intent(context, homeItem.getItemActivityClass())
                    if (homeItem == HomeScreenItem.PPG_IR || homeItem == HomeScreenItem.PPG_RED) {
                        intent.putExtra(MainActivity.PPG_BUNDLE_KEY, homeItem.name)
                    }
                    context.startActivity(intent)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(HomeScreenItemBackground, stringResource(id = R.string.note)) {
            context.startActivity(Intent(context, NoteActivity::class.java))
        }
        Spacer(modifier = Modifier.height(8.dp))
        AppButton(HomeScreenItemBackground, stringResource(id = R.string.settings)) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
        if (BuildConfig.ENABLE_INSTANT_SYNC_BUTTON) {
            Spacer(modifier = Modifier.height(8.dp))
            AppButton(HomeScreenItemBackground, stringResource(id = R.string.sync)) {
                WorkManager.getInstance(context).enqueue(
                    OneTimeWorkRequestBuilder<SyncPrivDataWorker>().build()
                )
                Toast.makeText(context, context.resources.getString(R.string.synchronizing), Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(modifier = Modifier.height(53.dp))
    }
}
