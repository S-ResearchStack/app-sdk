package researchstack.domain.model.eligibilitytest

import researchstack.domain.model.task.taskresult.SurveyResult

data class EligibilityTestResult(
    val studyId: String,
    val surveyResult: SurveyResult
)
