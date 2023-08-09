package com.nikcapko.memo.data.db.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.data.db.data.WordDBEntity
import javax.inject.Inject

internal class WordDBEntityMapper @Inject constructor() :
    EntityMapper<WordDBEntity, WordModel>,
    EntityListMapper<WordDBEntity, WordModel> {

    override fun mapFromEntity(entity: WordDBEntity): WordModel {
        return WordModel(
            id = entity.id,
            word = entity.word.orEmpty(),
            translation = entity.translation.orEmpty(),
            frequency = entity.frequency ?: 0f,
        )
    }

    override fun mapToEntity(model: WordModel): WordDBEntity {
        return WordDBEntity(
            id = model.id,
            word = model.word,
            translation = model.translation,
            frequency = model.frequency,
        )
    }

    override fun mapFromEntityList(initial: List<WordDBEntity>): List<WordModel> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<WordModel>): List<WordDBEntity> {
        return initial.map { mapToEntity(it) }
    }
}
