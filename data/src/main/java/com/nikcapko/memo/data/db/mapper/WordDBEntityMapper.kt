package com.nikcapko.memo.data.db.mapper

import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.data.db.data.WordDBEntity
import javax.inject.Inject

class WordDBEntityMapper @Inject constructor() : EntityMapper<WordDBEntity, WordModel> {

    override fun mapFromEntity(entity: WordDBEntity): WordModel {
        return WordModel(
            id = entity.id,
            word = entity.word.orEmpty(),
            type = entity.type.orEmpty(),
            gender = entity.gender.orEmpty(),
            translation = entity.translation.orEmpty(),
            frequency = entity.frequency ?: 0f,
            forms = emptyList(),
            primaryLanguage = entity.primaryLanguage
        )
    }

    override fun mapToEntity(model: WordModel): WordDBEntity {
        return WordDBEntity(
            id = model.id,
            word = model.word,
            type = model.type,
            gender = model.gender,
            translation = model.translation,
            frequency = model.frequency,
            primaryLanguage = model.primaryLanguage
        )
    }
}
