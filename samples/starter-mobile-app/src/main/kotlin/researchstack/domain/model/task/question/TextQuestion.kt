package researchstack.domain.model.task.question

import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionTag
import researchstack.domain.model.task.question.common.QuestionType

class TextQuestion(
    id: String,
    title: String,
    explanation: String,
    tag: QuestionTag,
    isRequired: Boolean,
) : Question(id, title, explanation, tag, isRequired, QuestionType.TEXT)
