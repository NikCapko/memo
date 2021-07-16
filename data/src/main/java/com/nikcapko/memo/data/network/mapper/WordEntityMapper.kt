package com.nikcapko.memo.data.network.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.data.network.data.WordEntity
import javax.inject.Inject

class WordEntityMapper @Inject constructor(
    private var formEntityMapper: FormEntityMapper,
) :
    EntityMapper<WordEntity, WordModel>,
    EntityListMapper<WordEntity, WordModel> {

    override fun mapFromEntity(entity: WordEntity): WordModel {
        return WordModel(
            id = entity.id,
            word = entity.word.orEmpty(),
            type = entity.type.orEmpty(),
            gender = entity.gender.orEmpty(),
            translation = entity.translation.orEmpty(),
            frequency = entity.frequency ?: 0f,
            primaryLanguage = entity.primaryLanguage,
            forms = formEntityMapper.mapFromEntityList(entity.forms ?: emptyList()),
        )
    }

    override fun mapToEntity(model: WordModel): WordEntity {
        return WordEntity(
            id = model.id,
            word = model.word,
            type = model.type,
            gender = model.gender,
            translation = model.translation,
            frequency = model.frequency,
            primaryLanguage = model.primaryLanguage,
            forms = formEntityMapper.mapToEntityList(model.forms),
        )
    }

    override fun mapFromEntityList(initial: List<WordEntity>): List<WordModel> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<WordModel>): List<WordEntity> {
        return initial.map { mapToEntity(it) }
    }
}
