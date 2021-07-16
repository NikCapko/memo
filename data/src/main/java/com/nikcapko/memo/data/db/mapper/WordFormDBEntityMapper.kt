package com.nikcapko.memo.data.db.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.data.db.data.WordFormDBEntity
import javax.inject.Inject

class WordFormDBEntityMapper @Inject constructor(
    private var formDBEntityMapper: FormDBEntityMapper,
) :
    EntityMapper<WordFormDBEntity, WordModel>,
    EntityListMapper<WordFormDBEntity, WordModel> {

    override fun mapFromEntity(entity: WordFormDBEntity): WordModel {
        return WordModel(
            id = entity.word?.id ?: 0,
            word = entity.word?.word.orEmpty(),
            type = entity.word?.type.orEmpty(),
            gender = entity.word?.gender.orEmpty(),
            translation = entity.word?.translation.orEmpty(),
            frequency = entity.word?.frequency ?: 0f,
            forms = formDBEntityMapper.mapFromEntityList(entity.forms),
            primaryLanguage = entity.word?.primaryLanguage ?: false
        )
    }

    override fun mapToEntity(model: WordModel): WordFormDBEntity {
        return WordFormDBEntity(
            word = WordDBEntityMapper().mapToEntity(model),
            forms = model.forms.let { FormDBEntityMapper().mapToEntityList(it) }
        )
    }

    override fun mapFromEntityList(initial: List<WordFormDBEntity>): List<WordModel> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<WordModel>): List<WordFormDBEntity> {
        return initial.map { mapToEntity(it) }
    }
}
