package researchstack.domain.model.task

import researchstack.domain.model.task.question.common.Question

data class Section(
    val questions: List<Question>
)
