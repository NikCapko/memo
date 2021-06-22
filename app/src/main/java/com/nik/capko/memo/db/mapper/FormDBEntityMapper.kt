package com.nik.capko.memo.db.mapper

import com.nik.capko.memo.base.mapper.EntityListMapper
import com.nik.capko.memo.base.mapper.EntityMapper
import com.nik.capko.memo.data.Form
import com.nik.capko.memo.db.data.FormDBEntity
import javax.inject.Inject

class FormDBEntityMapper @Inject constructor() :
    EntityMapper<FormDBEntity, Form>,
    EntityListMapper<FormDBEntity, Form> {

    override fun mapFromEntity(entity: FormDBEntity): Form {
        return Form(
            key = entity.key,
            name = entity.name.orEmpty(),
            value = entity.value.orEmpty(),
        )
    }

    override fun mapToEntity(model: Form): FormDBEntity {
        return FormDBEntity(
            key = model.key,
            name = model.name,
            value = model.value,
            wordId = 0
        )
    }

    override fun mapFromEntityList(initial: List<FormDBEntity>): List<Form> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Form>): List<FormDBEntity> {
        return initial.map { mapToEntity(it) }
    }
}
