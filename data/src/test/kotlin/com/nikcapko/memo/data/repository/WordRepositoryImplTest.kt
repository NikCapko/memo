package com.nikcapko.memo.data.repository

import com.nikcapko.memo.domain.model.WordModel
import com.nikcapko.memo.domain.repository.WordRepository
import com.nikcapko.memo.data.database.AppDatabase
import com.nikcapko.memo.data.database.converter.WordDBEntityListConverter
import com.nikcapko.memo.data.database.converter.WordModelToEntityConverter
import com.nikcapko.memo.data.database.data.WordDBEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Tests for [WordRepositoryImpl]
 **/
@ExperimentalCoroutinesApi
internal class WordRepositoryImplTest {

    private var appDatabase: AppDatabase = mockk(relaxed = true)

    private var wordDBEntityListConverter: WordDBEntityListConverter = mockk()
    private var wordModelToEntityConverter: WordModelToEntityConverter = mockk()

    private var repository: WordRepository = WordRepositoryImpl(
        appDatabase = appDatabase,
        wordDBEntityListConverter = wordDBEntityListConverter,
        wordModelToEntityConverter = wordModelToEntityConverter,
    )

    private var wordModel = WordModel(
        id = 1468,
        word = "vituperatoribus",
        translate = "constituto",
        frequency = 2.3f
    )

    private var wordDBEntity = WordDBEntity(
        id = 1468,
        word = "vituperatoribus",
        translation = "constituto",
        frequency = 2.3f
    )

    @Test
    fun `on saveWord() call wordDao-insert`() = runTest {
        coEvery { appDatabase.wordDao().insert(any()) } just runs
        every { wordModelToEntityConverter.convert(wordModel) } returns wordDBEntity

        repository.saveWord(wordModel)

        coVerify(exactly = 1) { appDatabase.wordDao().insert(wordDBEntity) }
    }

    @Test
    fun `on deleteWord() call wordDao-deleteWordById`() = runTest {
        coEvery { appDatabase.wordDao().deleteWordById(any()) } just runs

        repository.deleteWord("1468")

        coVerify(exactly = 1) { appDatabase.wordDao().deleteWordById("1468") }
    }

    @Test
    fun `on getWordsFromDB() call wordDao-getAllWords`() = runTest {
        coEvery { appDatabase.wordDao().getAllWords() } returns emptyList()
        every { wordDBEntityListConverter.convert(emptyList()) } returns emptyList()

        repository.getWordsFromDB()

        coVerify(exactly = 1) { appDatabase.wordDao().getAllWords() }
    }

    @Test
    fun `on getWordsForGameByLimit() call wordDao-getAllWords`() = runTest {
        coEvery { appDatabase.wordDao().getWordsForGameByLimit(1) } returns emptyList()
        every { wordDBEntityListConverter.convert(emptyList()) } returns emptyList()

        repository.getWordsForGameByLimit(1)

        coVerify(exactly = 1) { appDatabase.wordDao().getWordsForGameByLimit(1) }
    }

    @Test
    fun `on getWordsForGameByLimit() call wordDao-removeAll`() = runTest {
        coEvery { appDatabase.wordDao().removeAll() } just runs

        repository.clearDatabase()

        coVerify(exactly = 1) { appDatabase.wordDao().removeAll() }
    }
}
