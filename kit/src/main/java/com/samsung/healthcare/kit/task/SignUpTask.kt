package com.samsung.healthcare.kit.task

import com.samsung.healthcare.kit.model.Model
import com.samsung.healthcare.kit.model.RegistrationCompletedModel
import com.samsung.healthcare.kit.model.SignUpModel
import com.samsung.healthcare.kit.step.RegistrationCompletedStep
import com.samsung.healthcare.kit.step.SignUpStep
import com.samsung.healthcare.kit.step.Step

open class SignUpTask private constructor(
    id: String,
    name: String,
    description: String,
    steps: List<Step<out Model, *>>,
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
        customStep: Step<out Model, *>? = null
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
