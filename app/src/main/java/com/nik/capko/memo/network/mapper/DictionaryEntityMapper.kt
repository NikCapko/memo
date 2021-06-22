package com.nik.capko.memo.network.mapper

import com.nik.capko.memo.base.mapper.EntityListMapper
import com.nik.capko.memo.base.mapper.EntityMapper
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.network.data.DictionaryEntity
import javax.inject.Inject

class DictionaryEntityMapper @Inject constructor() :
    EntityMapper<DictionaryEntity, Dictionary>,
    EntityListMapper<DictionaryEntity, Dictionary> {

    override fun mapFromEntity(entity: DictionaryEntity): Dictionary {
        return Dictionary(
            id = entity.id.orEmpty(),
            name = entity.name.orEmpty(),
            size = entity.size,
        )
    }

    override fun mapToEntity(model: Dictionary): DictionaryEntity {
        return DictionaryEntity(
            id = model.id,
            name = model.name,
            size = model.size,
        )
    }

    override fun mapFromEntityList(initial: List<DictionaryEntity>): List<Dictionary> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Dictionary>): List<DictionaryEntity> {
        return initial.map { mapToEntity(it) }
    }
}
