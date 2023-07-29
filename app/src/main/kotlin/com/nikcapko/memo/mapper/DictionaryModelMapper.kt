package com.nikcapko.memo.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.DictionaryModel
import com.nikcapko.memo.data.Dictionary
import javax.inject.Inject

class DictionaryModelMapper @Inject constructor() :
    EntityMapper<DictionaryModel, Dictionary>,
    EntityListMapper<DictionaryModel, Dictionary> {

    override fun mapFromEntity(entity: DictionaryModel): Dictionary {
        return Dictionary(
            id = entity.id,
            name = entity.name,
            size = entity.size,
        )
    }

    override fun mapToEntity(model: Dictionary): DictionaryModel {
        return DictionaryModel(
            id = model.id,
            name = model.name,
            size = model.size,
        )
    }

    override fun mapFromEntityList(initial: List<DictionaryModel>): List<Dictionary> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Dictionary>): List<DictionaryModel> {
        return initial.map { mapToEntity(it) }
    }
}
