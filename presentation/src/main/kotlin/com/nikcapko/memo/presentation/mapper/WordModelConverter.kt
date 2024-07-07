package com.nikcapko.memo.presentation.mapper

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.BaseConverter
import com.nikcapko.memo.core.data.Word
import javax.inject.Inject

internal class WordModelConverter @Inject constructor() : BaseConverter<WordModel, Word> {

    override fun convert(from: WordModel): Word = with(from) {
        Word(
            id = id,
            word = word,
            translation = translation,
            frequency = frequency,
        )
    }
}
