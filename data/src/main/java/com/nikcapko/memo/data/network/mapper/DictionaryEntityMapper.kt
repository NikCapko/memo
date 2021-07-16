package com.nikcapko.memo.data.network.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.DictionaryModel
import com.nikcapko.memo.data.network.data.DictionaryEntity
import javax.inject.Inject

class DictionaryEntityMapper @Inject constructor() :
    EntityMapper<DictionaryEntity, DictionaryModel>,
    EntityListMapper<DictionaryEntity, DictionaryModel> {

    override fun mapFromEntity(entity: DictionaryEntity): DictionaryModel {
        return DictionaryModel(
            id = entity.id.orEmpty(),
            name = entity.name.orEmpty(),
            size = entity.size,
        )
    }

    override fun mapToEntity(model: DictionaryModel): DictionaryEntity {
        return DictionaryEntity(
            id = model.id,
            name = model.name,
            size = model.size,
        )
    }

    override fun mapFromEntityList(initial: List<DictionaryEntity>): List<DictionaryModel> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<DictionaryModel>): List<DictionaryEntity> {
        return initial.map { mapToEntity(it) }
    }
}
