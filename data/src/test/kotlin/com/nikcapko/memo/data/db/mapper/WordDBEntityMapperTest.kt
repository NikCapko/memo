package com.nikcapko.memo.data.db.mapper

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.data.db.data.WordDBEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Tests for [WordDBEntityMapper]
 */
class WordDBEntityMapperTest {

    private val mapper = WordDBEntityMapper()

    private var wordDBEntity = WordDBEntity(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f
    )

    private var wordModel = WordModel(
        id = 3929,
        word = "expetenda",
        translation = "vituperatoribus",
        frequency = 2.3f
    )

    @Test
    fun `test mapper mapToEntity`() {
        val result = mapper.mapToEntity(wordModel)
        Assertions.assertEquals(result, wordDBEntity)
    }

    @Test
    fun `test mapper mapFromEntity`() {
        val result = mapper.mapFromEntity(wordDBEntity)
        Assertions.assertEquals(result, wordModel)
    }

    @Test
    fun `test mapper mapToEntityList`() {
        val result = mapper.mapToEntityList(listOf(wordModel))
        Assertions.assertEquals(result, listOf(wordDBEntity))
    }

    @Test
    fun `test mapper mapFromEntityList`() {
        val result = mapper.mapFromEntityList(listOf(wordDBEntity))
        Assertions.assertEquals(result, listOf(wordModel))
    }
}
