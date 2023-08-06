package com.nikcapko.domain.model

data class WordModel(
    var id: Long,
    var word: String,
    var translation: String,
    var frequency: Float,
)
