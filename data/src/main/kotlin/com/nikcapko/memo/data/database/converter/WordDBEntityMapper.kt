package com.nikcapko.memo.data.database.converter

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.BaseConverter
import com.nikcapko.memo.core.common.converter.BaseListConverter
import com.nikcapko.memo.data.database.data.WordDBEntity
import javax.inject.Inject

internal class WordDBEntityListConverter @Inject constructor(
    wordDBEntityItemConverter: WordDBEntityItemConverter,
) : BaseListConverter<WordDBEntity, WordModel> {
    override val itemConverter: BaseConverter<WordDBEntity, WordModel> = wordDBEntityItemConverter
}

internal class WordDBEntityItemConverter @Inject constructor() : BaseConverter<WordDBEntity, WordModel> {
    override fun convert(from: WordDBEntity): WordModel = with(from) {
        WordModel(
            id = id,
            word = word.orEmpty(),
            translate = translation.orEmpty(),
            frequency = frequency ?: 0.0f,
        )
    }
}
