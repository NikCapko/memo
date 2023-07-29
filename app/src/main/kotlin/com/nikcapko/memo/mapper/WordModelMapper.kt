package com.nikcapko.memo.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.data.Word
import javax.inject.Inject

class WordModelMapper @Inject constructor(
    private var formModelMapper: FormModelMapper,
) :
    EntityMapper<WordModel, Word>,
    EntityListMapper<WordModel, Word> {

    override fun mapFromEntity(entity: WordModel): Word {
        return Word(
            id = entity.id,
            word = entity.word,
            type = entity.type,
            gender = entity.gender,
            translation = entity.translation,
            frequency = entity.frequency,
            primaryLanguage = entity.primaryLanguage,
            forms = formModelMapper.mapFromEntityList(entity.forms),
        )
    }

    override fun mapToEntity(model: Word): WordModel {
        return WordModel(
            id = model.id,
            word = model.word,
            type = model.type,
            gender = model.gender,
            translation = model.translation,
            frequency = model.frequency,
            primaryLanguage = model.primaryLanguage,
            forms = formModelMapper.mapToEntityList(model.forms),
        )
    }

    override fun mapFromEntityList(initial: List<WordModel>): List<Word> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Word>): List<WordModel> {
        return initial.map { mapToEntity(it) }
    }
}
