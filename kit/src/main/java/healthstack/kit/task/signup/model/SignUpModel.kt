package healthstack.kit.task.signup.model

import healthstack.kit.auth.SignInProvider
import healthstack.kit.task.base.StepModel

open class SignUpModel(
    id: String,
    title: String,
    val providers: List<SignInProvider>,
    val description: String? = null,
    drawableId: Int? = null,
) : StepModel(id, title, drawableId) {
    init {
        require(providers.isNotEmpty()) {
            "At least one provider is required"
        }
    }
}
