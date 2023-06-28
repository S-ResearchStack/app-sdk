package healthstack.backend.integration.task

import com.google.gson.annotations.SerializedName

/**
 * Stores the information of the task received from backend.
 *
 * @property revisionId If the task is modified, the modified ID for history management.
 * @property taskId ID of the task.
 * @property title Title of the task.
 * @property description Description of the task.
 * @property schedule Schedule of the task. (CronQuartz format)
 * @property startTime Time to activate task.
 * @property endTime Expiration time when task is no longer exposed.
 * @property validTime The time given to the user to perform the task.
 * @property items Items of the task
 */
data class TaskSpec(
    val revisionId: Int,
    @SerializedName("id")
    val taskId: String,
    val title: String,
    val type: String,
    val description: String?,
    val schedule: String,
    val startTime: String,
    val endTime: String,
    val validTime: Long,
    val items: List<Item>,
) {
    init {
        require(0 <= validTime)
        require(title.isNotBlank())
    }
}

/**
 * Stores the information of the item received from backend.
 *
 * @property name Name of the item.
 * @property type Type of the item.
 * @property contents Contents of the item. [Contents]
 * @property sequence Sequence of the item. (For ordering)
 */
data class Item(
    val name: String,
    val type: String,
    val contents: Contents,
    val sequence: Int,
)

/**
 * Stores the information of the contents received from backend.
 *
 * @property type Type of the content.
 * @property title Title of the content.
 * @property explanation Explanation of the content.
 * @property itemProperties Properties(tag) of the content. [ItemProperties]
 * @property required If item is required or not.
 */
data class Contents(
    val type: String,
    val required: Boolean,
    val title: String? = null,
    val explanation: String? = null,
    @SerializedName("properties")
    val itemProperties: ItemProperties? = null,
    val completionTitle: String? = null,
    val completionDescription: String? = null,
)

/**
 * System distinguishes the UI component based on the tag.
 *
 * @property tag Type of UI component to render. (ex) Radio, Dropdown
 */
open class ItemProperties(
    val tag: String,
    @SerializedName("skip_logic")
    val skipLogic: List<SkipLogic>?,
)

/**
 * System distinguishes the UI component based on the tag.
 *
 * @property tag Type of UI component to render. (ex) Radio, Dropdown
 */
class TextProperties(
    tag: String,
    skipLogic: List<SkipLogic>? = null,
) : ItemProperties(tag, skipLogic)

/**
 * Item properties for the choice question.
 *
 * @property options Values given as options. [Option]
 * @param tag Type of UI component to render. (ex) Radio, Dropdown
 */
class ChoiceProperties(
    tag: String,
    skipLogic: List<SkipLogic>? = null,
    val options: List<Option>,
) : ItemProperties(tag, skipLogic)

/**
 * Item properties for the ranking question.
 *
 * @property options Values given as options. [Option]
 * @param tag Type of UI component to render.
 */
class RankingProperties(
    tag: String,
    skipLogic: List<SkipLogic>? = null,
    val options: List<Option>,
) : ItemProperties(tag, skipLogic)

/**
 * Item properties for the date/time question.
 *
 * @property options Values given as options. [Option]
 * @param isTime if format of the answer is time
 * @param isDate if format of the answer is date
 * @param isRange if format of the answer is range
 */
class DateTimeProperties(
    tag: String,
    skipLogic: List<SkipLogic>? = null,
    val isTime: Boolean,
    val isDate: Boolean,
    val isRange: Boolean,
) : ItemProperties(tag, skipLogic)

/**
 * Item properties for the scale question.
 *
 * @property low Minimum number in Scale.
 * @property high Maximum number in Scale.
 * @property lowLabel Label of minimum number in Scale.
 * @property highLabel Label of maximum number in Scale.
 * @param tag Type of UI component to render. (ex) Radio, Dropdown
 */
class ScaleProperties(
    tag: String,
    skipLogic: List<SkipLogic>? = null,
    val low: Int,
    val high: Int,
    val lowLabel: String?,
    val highLabel: String?,
) : ItemProperties(tag, skipLogic)

/**
 * Stores the option.
 *
 * @property value Values given as options.
 */
data class Option(
    val value: String,
    val label: String? = null,
)

data class SkipLogic(
    val condition: String,
    val goToItemSequence: Int,
)
