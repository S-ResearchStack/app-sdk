package healthstack.kit.info.publication

import androidx.compose.runtime.Composable
import healthstack.kit.info.publication.content.ContentBlock
import healthstack.kit.ui.EducationCard

abstract class Publication(
    val coverContent: String?,
    val category: String,
    val title: String,
    val description: String,
    val contents: List<ContentBlock>,
) {
    @Composable
    abstract fun Render(
        onClick: (Publication?) -> Unit
    )

    @Composable
    abstract fun PreviewImage()

    @Composable
    fun RelatedContent() {
        // TODO: receive & render cardview for related publications, if any exist
    }

    @Composable
    fun CardView(onClick: (Publication?) -> Unit) {
        EducationCard(
            type = category,
            title = title,
            description = description,
            previewContent = { PreviewImage() },
            onClick = { onClick(this) }
        )
    }
}
