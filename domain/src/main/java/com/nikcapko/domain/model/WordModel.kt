package com.nikcapko.domain.model

data class WordModel(
    var id: Long,
    var word: String,
    var type: String,
    var gender: String,
    var translation: String,
    var frequency: Float,
    var primaryLanguage: Boolean = false,

    var forms: List<FormModel> = emptyList(),
)
