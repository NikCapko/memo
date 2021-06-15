package com.nik.capko.memo.db.mapper

import com.nik.capko.memo.base.mapper.EntityMapper
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.data.WordDBEntity
import javax.inject.Inject

class WordDBMapper @Inject constructor() : EntityMapper<WordDBEntity, Word> {

    override fun mapFromEntity(entity: WordDBEntity): Word {
        return Word(
            id = entity.id,
            word = entity.word,
            type = entity.type,
            gender = entity.gender,
            translation = entity.translation,
            frequency = entity.frequency,
            forms = emptyList(),
            primaryLanguage = entity.primaryLanguage
        )
    }

    override fun mapToEntity(model: Word): WordDBEntity {
        return WordDBEntity(
            id = model.id,
            word = model.word,
            type = model.type,
            gender = model.gender,
            translation = model.translation,
            frequency = model.frequency,
            primaryLanguage = model.primaryLanguage
        )
    }
}
