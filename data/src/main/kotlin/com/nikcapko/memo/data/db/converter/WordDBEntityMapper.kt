package com.nikcapko.memo.data.db.converter

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.BaseConverter
import com.nikcapko.memo.core.common.converter.BaseListConverter
import com.nikcapko.memo.data.db.data.WordDBEntity
import javax.inject.Inject

internal class WordDBEntityListConverter @Inject constructor(
    private val wordDBEntityItemConverter: WordDBEntityItemConverter,
) : BaseListConverter<WordDBEntity, WordModel> {
    override val itemConverter: BaseConverter<WordDBEntity, WordModel> = wordDBEntityItemConverter
}

internal class WordDBEntityItemConverter @Inject() constructor() : BaseConverter<WordDBEntity, WordModel> {
    override fun convert(from: WordDBEntity): WordModel = with(from) {
        WordModel(
            id = id,
            word = word.orEmpty(),
            translation = translation.orEmpty(),
            frequency = frequency ?: 0.0f,
        )
    }
}
