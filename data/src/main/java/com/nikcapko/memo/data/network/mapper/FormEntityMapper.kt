package com.nikcapko.memo.data.network.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.FormModel
import com.nikcapko.memo.data.network.data.FormEntity
import javax.inject.Inject

class FormEntityMapper @Inject constructor() :
    EntityMapper<FormEntity, FormModel>,
    EntityListMapper<FormEntity, FormModel> {

    override fun mapFromEntity(entity: FormEntity): FormModel {
        return FormModel(
            key = entity.key,
            name = entity.name.orEmpty(),
            value = entity.value.orEmpty(),
        )
    }

    override fun mapToEntity(model: FormModel): FormEntity {
        return FormEntity(
            key = model.key,
            name = model.name,
            value = model.value,
        )
    }

    override fun mapFromEntityList(initial: List<FormEntity>): List<FormModel> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<FormModel>): List<FormEntity> {
        return initial.map { mapToEntity(it) }
    }
}
