package com.nik.capko.memo.base.mapper

interface EntityListMapper<Entity, Model> {
    fun mapFromEntityList(initial: List<Entity>): List<Model>
    fun mapToEntityList(initial: List<Model>): List<Entity>
}
