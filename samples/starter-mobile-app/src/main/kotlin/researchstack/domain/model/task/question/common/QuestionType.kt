package researchstack.domain.model.task.question.common

import researchstack.domain.model.task.question.common.QuestionTag.CHECKBOX
import researchstack.domain.model.task.question.common.QuestionTag.DROPDOWN
import researchstack.domain.model.task.question.common.QuestionTag.IMAGE
import researchstack.domain.model.task.question.common.QuestionTag.RADIO

enum class QuestionType(val tags: List<QuestionTag>) {
    CHOICE(listOf(RADIO, CHECKBOX, IMAGE, DROPDOWN)),
    DATETIME(listOf(QuestionTag.DATETIME)),
    RANK(listOf(QuestionTag.RANK)),
    SCALE(listOf(QuestionTag.SLIDER)),
    TEXT(listOf(QuestionTag.TEXT))
}
