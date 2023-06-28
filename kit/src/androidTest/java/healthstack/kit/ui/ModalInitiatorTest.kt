package healthstack.kit.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue.Expanded
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class ModalInitiatorTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun testModalInitiator() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val state = rememberModalBottomSheetState(
                    initialValue = Hidden,
                    skipHalfExpanded = true,
                )
                val value = remember { mutableStateOf("") }
                val changeValue = { newVal: String -> value.value = newVal }

                ModalInitiator(
                    id = "id",
                    title = "modal title",
                    placeholder = "modal placeholder",
                    state = state,
                    selectedValue = value,
                    changeModal = changeValue
                )
            }
        }

        rule.onNodeWithText("modal placeholder")
            .assertExists()
            .performClick()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun testChoiceModal() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val state = rememberModalBottomSheetState(
                    initialValue = Expanded,
                    skipHalfExpanded = true,
                )
                val value = remember { mutableStateOf("") }
                val changeValue = { newVal: String -> value.value = newVal }

                ChoiceModal(
                    title = "title",
                    values = listOf("A", "B", "C", "D"),
                    changeValue = changeValue,
                    state = state
                )
            }
        }
        rule.onNodeWithText("Cancel")
            .assertExists()

        rule.onNodeWithText("Save")
            .assertExists()
            .performClick()

        rule.onNodeWithText("A")
            .assertExists()
            .performClick()
    }

    @Test
    fun testCalendarModal() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val value = remember { mutableStateOf("") }
                val changeValue = { newVal: String -> value.value = newVal }

                CalendarModalInitiator(
                    title = "title",
                    placeholder = "calendar placeholder",
                    updateValue = changeValue
                )
            }
        }

        rule.onNodeWithText("calendar placeholder")
            .assertExists()
            .performClick()

        rule.onNodeWithTag("calendar")
            .assertExists()
            .performClick()
    }

    @Test
    fun testTimeModal() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val value = remember { mutableStateOf("") }
                val changeValue = { newVal: String -> value.value = newVal }

                TimePickerInitiator(
                    title = "title",
                    placeholder = "timepicker placeholder",
                    updateValue = changeValue
                )
            }
        }

        rule.onNodeWithText("timepicker placeholder")
            .assertExists()
            .performClick()

        rule.onNodeWithTag("timepicker")
            .assertExists()
            .performClick()
    }
}
