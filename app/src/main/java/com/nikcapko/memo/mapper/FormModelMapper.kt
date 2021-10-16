package com.nikcapko.memo.mapper

import com.nikcapko.core.mapper.EntityListMapper
import com.nikcapko.core.mapper.EntityMapper
import com.nikcapko.domain.model.FormModel
import com.nikcapko.memo.data.Form
import javax.inject.Inject

class FormModelMapper @Inject constructor() :
    EntityMapper<FormModel, Form>,
    EntityListMapper<FormModel, Form> {

    override fun mapFromEntity(entity: FormModel): Form {
        return Form(
            key = entity.key,
            name = entity.name,
            value = entity.value,
        )
    }

    override fun mapToEntity(model: Form): FormModel {
        return FormModel(
            key = model.key,
            name = model.name,
            value = model.value,
        )
    }

    override fun mapFromEntityList(initial: List<FormModel>): List<Form> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Form>): List<FormModel> {
        return initial.map { mapToEntity(it) }
    }
}
