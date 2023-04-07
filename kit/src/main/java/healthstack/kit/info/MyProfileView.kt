package healthstack.kit.info

import android.content.res.Resources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import healthstack.kit.R
import healthstack.kit.datastore.PreferenceDataStore
import healthstack.kit.info.MyProfileView.ProfileField.AGE
import healthstack.kit.info.MyProfileView.ProfileField.COUNTRY
import healthstack.kit.info.MyProfileView.ProfileField.DOB
import healthstack.kit.info.MyProfileView.ProfileField.GENDER
import healthstack.kit.info.MyProfileView.ProfileField.RACE
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.CalendarModalInitiator
import healthstack.kit.ui.ChoiceModal
import healthstack.kit.ui.ModalInitiator
import healthstack.kit.ui.TopBar
import kotlinx.coroutines.flow.first
import org.json.JSONException
import org.json.JSONObject

class MyProfileView(
    val onClickBack: () -> Unit = {},
) {

    enum class ProfileField(val id: String) {
        AGE("age"),
        GENDER("gender"),
        COUNTRY("country"),
        RACE("race"),
        DOB("dob")
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Render() {
        val context = LocalContext.current
        val preferenceDataStore = PreferenceDataStore(context)
        val profile = remember { mutableStateOf("") }
        val changeProfile = { newValue: String -> profile.value = newValue }
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val resources: Resources = context.resources

        val fromEligibility = resources.getStringArray(R.array.from_eligibility).toList()
        val profileFields = resources.getStringArray(R.array.profile_field).toList()

        LaunchedEffect(Unit) {
            profile.value = preferenceDataStore.profile.first()
        }

        val json = if (profile.value.isEmpty()) JSONObject() else JSONObject(profile.value)

        Scaffold(
            topBar = {
                TopBar("Profile") {
                    onClickBack()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                val state = rememberModalBottomSheetState(
                    initialValue = ModalBottomSheetValue.Hidden,
                    skipHalfExpanded = true,
                )

                val age = remember { mutableStateOf(json.getOrEmpty(AGE.id)) }
                val gender = remember { mutableStateOf(json.getOrEmpty(GENDER.id)) }
                val race = remember { mutableStateOf(json.getOrEmpty(RACE.id)) }
                val country = remember { mutableStateOf(json.getOrEmpty(COUNTRY.id)) }
                val dob = remember { mutableStateOf(json.getOrEmpty(DOB.id)) }
                val modal = remember { mutableStateOf("") }

                // create function that can change selectedValue
                val changeAge = { input: String -> age.value = input }
                val changeGender = { input: String -> gender.value = input }
                val changeRace = { input: String -> race.value = input }
                val changeCountry = { input: String -> country.value = input }
                val changeBirthday = { input: String -> dob.value = input }
                val changeModal = { input: String -> modal.value = input }

                if (age.value.isNotEmpty()) json.put(AGE.id, age.value)
                if (gender.value.isNotEmpty()) json.put(GENDER.id, gender.value)
                if (race.value.isNotEmpty()) json.put(RACE.id, race.value)
                if (country.value.isNotEmpty()) json.put(COUNTRY.id, country.value)
                if (dob.value.isNotEmpty()) json.put(DOB.id, dob.value)

                changeProfile(json.toString())

                ModalBottomSheetLayout(
                    sheetState = state,
                    scrimColor = AppTheme.colors.onSurface.copy(0.38F),
                    sheetContent = {
                        when (modal.value) {
                            COUNTRY.id -> ChoiceModal(
                                resources.getString(R.string.country_title),
                                resources.getStringArray(R.array.profile_countries).toList(),
                                changeCountry, state
                            )

                            RACE.id -> ChoiceModal(
                                resources.getString(R.string.race_title),
                                resources.getStringArray(R.array.profile_races).toList(),
                                changeRace, state
                            )

                            GENDER.id -> ChoiceModal(
                                resources.getString(R.string.gender_title),
                                resources.getStringArray(R.array.profile_genders).toList(),
                                changeGender, state
                            )

                            else -> Text("")
                        }
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp),
                    ) {
                        ProfileTextView(resources.getString(R.string.id_title), "${currentUser?.displayName}")
                        ProfileTextView(resources.getString(R.string.email_title), "${currentUser?.email}")

                        if (profileFields.contains(AGE.id)) {
                            if (fromEligibility.contains(AGE.id)) {
                                ProfileTextView(
                                    resources.getString(R.string.age_title),
                                    json.getOrEmpty(AGE.id)
                                )
                            } else {
                                ProfileTextView(
                                    resources.getString(R.string.age_title),
                                    resources.getString(R.string.age_placeholder),
                                    false,
                                    changeAge
                                )
                            }
                        }

                        if (profileFields.contains(GENDER.id)) {
                            if (fromEligibility.contains(GENDER.id))
                                ProfileTextView(
                                    resources.getString(R.string.gender_title),
                                    json.getOrEmpty(GENDER.id)
                                )
                            else
                                ModalInitiator(
                                    GENDER.id,
                                    resources.getString(R.string.gender_title),
                                    resources.getString(R.string.gender_placeholder),
                                    state, gender,
                                    changeModal
                                )
                        }

                        if (profileFields.contains(RACE.id)) {
                            if (fromEligibility.contains(RACE.id))
                                ProfileTextView(
                                    resources.getString(R.string.race_title),
                                    json.getOrEmpty(RACE.id)
                                )
                            else
                                ModalInitiator(
                                    RACE.id,
                                    resources.getString(R.string.race_title),
                                    resources.getString(R.string.race_placeholder),
                                    state, race,
                                    changeModal
                                )
                        }

                        if (profileFields.contains(COUNTRY.id)) {
                            if (fromEligibility.contains(COUNTRY.id))
                                ProfileTextView(
                                    resources.getString(R.string.country_title),
                                    json.getString(COUNTRY.id)
                                )
                            else
                                ModalInitiator(
                                    COUNTRY.id,
                                    resources.getString(R.string.country_title),
                                    resources.getString(R.string.country_placeholder),
                                    state, country, changeModal
                                )
                        }

                        if (profileFields.contains(DOB.id)) {
                            if (fromEligibility.contains(DOB.id))
                                ProfileTextView(resources.getString(R.string.dob_title), json.getString(DOB.id))
                            else
                                CalendarModalInitiator(
                                    resources.getString(R.string.dob_title),
                                    resources.getString(R.string.dob_placeholder),
                                    changeBirthday
                                )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileTextView(
    title: String,
    value: String,
    disabled: Boolean = true,
    changeValue: (String) -> Unit = {},
) {
    val currentValue = remember { mutableStateOf("") }

    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth(),
        style = AppTheme.typography.body3,
        color = if (disabled) AppTheme.colors.onBackground.copy(0.38F) else AppTheme.colors.onBackground.copy(0.6F)
    )
    Spacer(Modifier.height(8.dp))
    if (disabled) {
        Text(
            text = value,
            modifier = Modifier
                .fillMaxWidth(),
            style = AppTheme.typography.body1,
            color = AppTheme.colors.onBackground.copy(0.38F)
        )
    } else {
        BasicTextField(
            value = currentValue.value,
            onValueChange = {
                currentValue.value = it
                changeValue(it)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTheme.typography.body1.copy(
                color = AppTheme.colors.onBackground
            ),
            decorationBox = {
                if (currentValue.value.isEmpty()) Text(
                    value,
                    color = AppTheme.colors.onBackground.copy(0.6F),
                    style = AppTheme.typography.body1
                )
                it()
            }
        )
    }

    Spacer(Modifier.height(4.dp))
    Divider(
        color = if (disabled) AppTheme.colors.onBackground.copy(0.38F)
        else if (currentValue.value.isEmpty()) AppTheme.colors.onBackground.copy(0.6F)
        else AppTheme.colors.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(32.dp))
}

fun JSONObject.getOrEmpty(id: String): String =
    try {
        this.getString(id)
    } catch (e: JSONException) {
        String()
    }
