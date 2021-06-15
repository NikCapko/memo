package com.nik.capko.memo.network.mapper

import com.nik.capko.memo.base.mapper.EntityListMapper
import com.nik.capko.memo.base.mapper.EntityMapper
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.network.data.WordEntity
import javax.inject.Inject

class WordEntityMapper @Inject constructor(
    private var formEntityMapper: FormEntityMapper,
) :
    EntityMapper<WordEntity, Word>,
    EntityListMapper<WordEntity, Word> {

    override fun mapFromEntity(entity: WordEntity): Word {
        return Word(
            id = entity.id,
            word = entity.word,
            type = entity.type,
            gender = entity.gender,
            translation = entity.translation,
            frequency = entity.frequency,
            primaryLanguage = entity.primaryLanguage,
            forms = formEntityMapper.mapFromEntityList(entity.forms ?: emptyList()),
        )
    }

    override fun mapToEntity(model: Word): WordEntity {
        return WordEntity(
            id = model.id,
            word = model.word,
            type = model.type,
            gender = model.gender,
            translation = model.translation,
            frequency = model.frequency,
            primaryLanguage = model.primaryLanguage,
            forms = formEntityMapper.mapToEntityList(model.forms ?: emptyList()),
        )
    }

    override fun mapFromEntityList(initial: List<WordEntity>): List<Word> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Word>): List<WordEntity> {
        return initial.map { mapToEntity(it) }
    }
}
