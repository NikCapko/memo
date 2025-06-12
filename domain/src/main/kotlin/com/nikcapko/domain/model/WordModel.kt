package com.nikcapko.domain.model

import java.io.Serializable

@Suppress("SerialVersionUIDInSerializableClass")
data class WordModel(
    var id: Long,
    var word: String,
    var translate: String,
    var frequency: Float,
) : Serializable
