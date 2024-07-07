package com.nikcapko.memo.presentation.mapper

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.BaseConverter
import com.nikcapko.memo.core.data.Word
import javax.inject.Inject

class WordToWordModelConverter @Inject constructor(): BaseConverter<Word, WordModel> {
    override fun convert(from: Word): WordModel = with(from) {
        WordModel(
            id = id,
            word = word,
            translation = translation,
            frequency = frequency,
        )
    }
}