package com.samsung.healthcare.kit.model

import com.samsung.healthcare.kit.auth.SignInProvider

class SignUpModel(
    id: String,
    title: String,
    val providers: List<SignInProvider>,
    val description: String? = null,
    drawableId: Int? = null,
) : Model(id, title, drawableId) {
    init {
        require(providers.isNotEmpty()) {
            "at least one provider is required"
        }
    }
}
