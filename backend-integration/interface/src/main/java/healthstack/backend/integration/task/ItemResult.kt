package healthstack.backend.integration.task

/**
 * Data Transfer Object for uploading the result of each item.
 *
 * @property itemName Name of the Item.
 * @property result Result of the Item.
 */
data class ItemResult(
    val itemName: String,
    val result: String,
)
