package com.samsung.healthcare.kit.sample.registration

import com.samsung.healthcare.kit.model.Model
import com.samsung.healthcare.kit.model.question.QuestionModel

class RegistrationModel(
    title: String,
    val questions: List<QuestionModel<*>>
) : Model("", title, null)
