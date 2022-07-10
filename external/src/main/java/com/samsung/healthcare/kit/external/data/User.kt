package com.samsung.healthcare.kit.external.data

data class User(
    val userId: String,
    val profile: Map<String, Any> = emptyMap(),
)
