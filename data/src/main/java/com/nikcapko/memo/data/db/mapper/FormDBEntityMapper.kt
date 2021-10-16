package com.nikcapko.memo.data.db.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.FormModel
import com.nikcapko.memo.data.db.data.FormDBEntity
import javax.inject.Inject

class FormDBEntityMapper @Inject constructor() :
    EntityMapper<FormDBEntity, FormModel>,
    EntityListMapper<FormDBEntity, FormModel> {

    override fun mapFromEntity(entity: FormDBEntity): FormModel {
        return FormModel(
            key = entity.key,
            name = entity.name.orEmpty(),
            value = entity.value.orEmpty(),
        )
    }

    override fun mapToEntity(model: FormModel): FormDBEntity {
        return FormDBEntity(
            key = model.key,
            name = model.name,
            value = model.value,
            wordId = 0
        )
    }

    override fun mapFromEntityList(initial: List<FormDBEntity>): List<FormModel> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<FormModel>): List<FormDBEntity> {
        return initial.map { mapToEntity(it) }
    }
}
