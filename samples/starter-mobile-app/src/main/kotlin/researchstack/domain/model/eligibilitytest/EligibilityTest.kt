package researchstack.domain.model.eligibilitytest

import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.task.Section

data class EligibilityTest(
    val studyId: String,
    val sections: List<Section>,
    val answers: List<Answer>,
)
