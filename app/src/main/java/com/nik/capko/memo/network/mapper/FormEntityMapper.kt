package com.nik.capko.memo.network.mapper

import com.nik.capko.memo.base.mapper.EntityListMapper
import com.nik.capko.memo.base.mapper.EntityMapper
import com.nik.capko.memo.data.Form
import com.nik.capko.memo.network.data.FormEntity
import javax.inject.Inject

class FormEntityMapper @Inject constructor() :
    EntityMapper<FormEntity, Form>,
    EntityListMapper<FormEntity, Form> {

    override fun mapFromEntity(entity: FormEntity): Form {
        return Form(
            key = entity.key,
            name = entity.name.orEmpty(),
            value = entity.value.orEmpty(),
        )
    }

    override fun mapToEntity(model: Form): FormEntity {
        return FormEntity(
            key = model.key,
            name = model.name,
            value = model.value,
        )
    }

    override fun mapFromEntityList(initial: List<FormEntity>): List<Form> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Form>): List<FormEntity> {
        return initial.map { mapToEntity(it) }
    }
}
