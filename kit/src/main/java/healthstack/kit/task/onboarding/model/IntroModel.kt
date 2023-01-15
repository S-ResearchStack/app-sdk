package healthstack.kit.task.onboarding.model

import healthstack.kit.R
import healthstack.kit.task.base.StepModel

open class IntroModel(
    id: String,
    title: String,
    drawableId: Int? = R.drawable.sample_image_alpha4,
    val logoDrawableId: Int? = null,
    val summaries: List<Pair<Int, String>>? = null,
    val sections: List<IntroSection>,
) : StepModel(id, title, drawableId) {

    data class IntroSection(val subTitle: String, val description: String)
}
