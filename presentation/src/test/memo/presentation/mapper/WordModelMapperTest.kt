package com.nikcapko.memo.mapper

import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.data.Word
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Tests for [WordModelMapper]
 */
internal class WordModelMapperTest {

    private val mapper = WordModelMapper()

    private var word = Word(
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
        val result = mapper.mapToEntity(word)
        Assertions.assertEquals(result, wordModel)
    }

    @Test
    fun `test mapper mapFromEntity`() {
        val result = mapper.mapFromEntity(wordModel)
        Assertions.assertEquals(result, word)
    }

    @Test
    fun `test mapper mapToEntityList`() {
        val result = mapper.mapToEntityList(listOf(word))
        Assertions.assertEquals(result, listOf(wordModel))
    }

    @Test
    fun `test mapper mapFromEntityList`() {
        val result = mapper.mapFromEntityList(listOf(wordModel))
        Assertions.assertEquals(result, listOf(word))
    }
}
