package com.samsung.healthcare.kit.task

import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.Model
import com.samsung.healthcare.kit.step.ConsentTextStep
import com.samsung.healthcare.kit.step.EligibilityCheckerStep
import com.samsung.healthcare.kit.step.EligibilityIntroStep
import com.samsung.healthcare.kit.step.EligibilityResultStep
import com.samsung.healthcare.kit.step.IntroStep
import com.samsung.healthcare.kit.step.Step

class OnboardingTask private constructor(
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
        introStep: IntroStep,
    ) : this(id, name, description, listOf(introStep))

    constructor(
        id: String,
        name: String,
        description: String,
        introStep: IntroStep,
        eligibilityIntroStep: EligibilityIntroStep,
        eligibilityCheckerStep: EligibilityCheckerStep,
        eligibilityResultStep: EligibilityResultStep,
        consentTextStep: ConsentTextStep
    ) : this(
        id, name, description,
        listOf(
            introStep,
            eligibilityIntroStep,
            eligibilityCheckerStep,
            eligibilityResultStep,
            consentTextStep,
        )
    )

    var eligibility: Boolean = false

    override val pageCallbacks =
        object : CallbackCollection() {
            override fun next() {
                if (hasNext()) pageableStream.postValue(++progress.current)
                else callback?.invoke()
            }

            override fun prev() {
                if (hasPrev()) pageableStream.postValue(--progress.current)
            }

            override fun setEligibility(value: Boolean) {
                eligibility = value
            }

            override fun getEligibility(): Boolean = eligibility
        }
}
