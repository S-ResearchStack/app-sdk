package healthstack.kit.task.signup

import healthstack.kit.task.base.OrderedTask
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.task.signup.model.RegistrationCompletedModel
import healthstack.kit.task.signup.model.SignUpModel
import healthstack.kit.task.signup.step.RegistrationCompletedStep
import healthstack.kit.task.signup.step.SignUpStep

open class SignUpTask private constructor(
    id: String,
    name: String,
    description: String,
    steps: List<Step<out StepModel, *>>,
) : OrderedTask(
    id,
    name,
    description,
    steps
) {
    constructor(
        id: String,
        name: String,
        description: String,
        signUpStep: SignUpModel,
        registrationCompletedModel: RegistrationCompletedModel,
        customStep: Step<out StepModel, *>? = null
    ) : this(
        id, name, description,
        listOfNotNull(
            SignUpStep(
                "$id-signup-step",
                "",
                signUpStep
            ),
            customStep,
            RegistrationCompletedStep(
                "$id-registration-step",
                "",
                registrationCompletedModel
            )
        )
    )
}
