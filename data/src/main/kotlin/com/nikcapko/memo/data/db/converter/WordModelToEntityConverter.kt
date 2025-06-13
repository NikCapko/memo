package com.nikcapko.memo.data.db.converter

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.BaseConverter
import com.nikcapko.memo.data.db.data.WordDBEntity
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
