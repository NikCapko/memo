package com.nik.capko.memo.db.mapper

import com.nik.capko.memo.base.mapper.EntityListMapper
import com.nik.capko.memo.base.mapper.EntityMapper
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.data.WordFormDBEntity
import javax.inject.Inject

class WordFormDBEntityMapper @Inject constructor(
    private var formDBEntityMapper: FormDBEntityMapper,
) :
    EntityMapper<WordFormDBEntity, Word>,
    EntityListMapper<WordFormDBEntity, Word> {

    override fun mapFromEntity(entity: WordFormDBEntity): Word {
        return Word(
            id = entity.word?.id ?: 0,
            word = entity.word?.word.orEmpty(),
            type = entity.word?.type.orEmpty(),
            gender = entity.word?.gender.orEmpty(),
            translation = entity.word?.translation.orEmpty(),
            frequency = entity.word?.frequency ?: 0f,
            forms = formDBEntityMapper.mapFromEntityList(entity.forms),
            primaryLanguage = entity.word?.primaryLanguage ?: false
        )
    }

    override fun mapToEntity(model: Word): WordFormDBEntity {
        return WordFormDBEntity(
            word = WordDBEntityMapper().mapToEntity(model),
            forms = model.forms.let { FormDBEntityMapper().mapToEntityList(it) }
        )
    }

    override fun mapFromEntityList(initial: List<WordFormDBEntity>): List<Word> {
        return initial.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(initial: List<Word>): List<WordFormDBEntity> {
        return initial.map { mapToEntity(it) }
    }
}
