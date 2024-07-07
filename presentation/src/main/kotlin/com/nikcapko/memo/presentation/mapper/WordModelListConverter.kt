package com.nikcapko.memo.presentation.mapper

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.BaseConverter
import com.nikcapko.memo.core.common.converter.BaseListConverter
import com.nikcapko.memo.core.data.Word
import javax.inject.Inject

internal class WordModelListConverter @Inject constructor(
    private val wordModelConverter: WordModelConverter,
) : BaseListConverter<WordModel, Word> {
    override val itemConverter: BaseConverter<WordModel, Word> = wordModelConverter
}
