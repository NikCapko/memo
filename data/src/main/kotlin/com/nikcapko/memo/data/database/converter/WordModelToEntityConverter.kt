package com.nikcapko.memo.data.database.converter

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.BaseConverter
import com.nikcapko.memo.data.database.data.WordDBEntity
import javax.inject.Inject

internal class WordModelToEntityConverter @Inject constructor() : BaseConverter<WordModel, WordDBEntity> {
    override fun convert(from: WordModel): WordDBEntity = with(from) {
        WordDBEntity(
            id = id,
            word = word,
            translation = translate,
            frequency = frequency,
        )
    }
}
