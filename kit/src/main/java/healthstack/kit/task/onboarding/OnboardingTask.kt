package healthstack.kit.task.onboarding

import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.OrderedTask
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.task.onboarding.step.ConsentTextStep
import healthstack.kit.task.onboarding.step.EligibilityCheckerStep
import healthstack.kit.task.onboarding.step.EligibilityIntroStep
import healthstack.kit.task.onboarding.step.EligibilityResultStep
import healthstack.kit.task.onboarding.step.IntroStep

class OnboardingTask private constructor(
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
