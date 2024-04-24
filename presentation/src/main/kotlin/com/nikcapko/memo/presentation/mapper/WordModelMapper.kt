package com.nikcapko.memo.presentation.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.common.data.Word
import javax.inject.Inject

internal class WordModelMapper @Inject constructor() :
    EntityMapper<WordModel, Word>,
    EntityListMapper<WordModel, Word> {

    override fun mapFromEntity(entity: WordModel): Word {
        return Word(
            id = entity.id,
            word = entity.word,
            translation = entity.translation,
            frequency = entity.frequency,
        )
    }

    override fun mapToEntity(model: Word): WordModel {
        return WordModel(
            id = model.id,
            word = model.word,
            translation = model.translation,
            frequency = model.frequency,
        )
    }

    override fun mapFromEntityList(initial: List<WordModel>): List<Word> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Word>): List<WordModel> {
        return initial.map { mapToEntity(it) }
    }
}
