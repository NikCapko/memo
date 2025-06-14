package com.nikcapko.memo.data.database.converter

import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.core.common.converter.convert
import com.nikcapko.memo.data.database.data.WordDBEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Tests for [WordDBEntityMapper]
 */
class WordDBBaseConverterTest {

    private val wordDBEntityItemConverter = WordDBEntityItemConverter()
    private val wordDBEntityListConverter = WordDBEntityListConverter(wordDBEntityItemConverter)

    private val wordModelToEntityConverter = WordModelToEntityConverter()

    private var wordDBEntity = WordDBEntity(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f
    )

    private var wordModel = WordModel(
        id = 3929,
        word = "expetenda",
        translate = "vituperatoribus",
        frequency = 2.3f
    )

    @Test
    fun `test mapper mapToEntity`() {
        val result = wordModel.convert(wordModelToEntityConverter)
        Assertions.assertEquals(result, wordDBEntity)
    }

    @Test
    fun `test mapper mapFromEntity`() {
        val result = wordDBEntity.convert(wordDBEntityItemConverter)
        Assertions.assertEquals(result, wordModel)
    }

    @Test
    fun `test mapper mapFromEntityList`() {
        val result = listOf(wordDBEntity).convert(wordDBEntityListConverter)
        Assertions.assertEquals(result, listOf(wordModel))
    }
}
