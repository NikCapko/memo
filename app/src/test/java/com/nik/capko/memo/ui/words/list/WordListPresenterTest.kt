package com.nik.capko.memo.ui.words.list

import com.github.terrakok.cicerone.Router
import com.nik.capko.memo.db.AppDatabase
import com.nik.capko.memo.db.mapper.WordDBEntityMapper
import com.nik.capko.memo.db.mapper.WordFormDBEntityMapper
import com.nik.capko.memo.network.ApiServiceImpl
import com.nik.capko.memo.network.mapper.WordEntityMapper
import com.nik.capko.memo.repository.WordRepository
import com.nik.capko.memo.utils.AppStorage
import com.nik.capko.memo.utils.Constants
import com.nik.capko.memo.utils.resources.FieldConverter
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class WordListPresenterTest {

    private val router = spyk<Router>()

    @RelaxedMockK
    private lateinit var appDatabase: AppDatabase

    @MockK
    private lateinit var apiService: ApiServiceImpl

    @RelaxedMockK
    private lateinit var wordFormDBEntityMapper: WordFormDBEntityMapper

    @MockK
    private lateinit var wordDBEntityMapper: WordDBEntityMapper

    @MockK
    private lateinit var wordEntityMapper: WordEntityMapper

    @MockK
    private lateinit var fieldConverter: FieldConverter

    @MockK
    private lateinit var appStorage: AppStorage

    private lateinit var wordRepository: WordRepository

    private lateinit var wordListPresenter: WordListPresenter

    val dispatcher = TestCoroutineDispatcher()

    lateinit var viewState: WordListView

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        viewState = spyk()
        every { appDatabase.wordDao() } returns mockk(relaxed = true)
        every { appDatabase.formDao() } returns mockk()
        wordRepository = WordRepository(
            appDatabase = appDatabase,
            apiService = apiService,
            wordFormDBEntityMapper = wordFormDBEntityMapper,
            wordDBEntityMapper = wordDBEntityMapper,
            wordEntityMapper = wordEntityMapper,
            fieldConverter = fieldConverter,
        )
        wordListPresenter = WordListPresenter(router, wordRepository, appStorage)
    }

    @Test
    fun `test load data on first attach view to presenter`() {
        coEvery { appDatabase.wordDao().getAllWords() } returns emptyList()

        wordListPresenter.attachView(viewState)

        coVerify(exactly = 1) { viewState.startLoading() }
        coVerify(exactly = 1) { viewState.showWords(emptyList()) }
        coVerify(exactly = 1) { viewState.completeLoading() }
    }

    @Test
    fun `test enable sound on click on item`() {
        coEvery { appDatabase.wordDao().getAllWords() } returns emptyList()

        wordListPresenter.attachView(viewState)
        wordListPresenter.onEnableSound(0)

        verify { viewState.speakOut(null) }
    }

    @Test
    fun `test logout`() {
        wordListPresenter.attachView(viewState)
        wordListPresenter.logout()

        verify { viewState.showClearDatabaseDialog() }
    }

    @Test
    fun `test logout with clear databases`() {
        every { appStorage.put(Constants.IS_REGISTER, false) } just runs
        verify { appStorage.put(Constants.IS_REGISTER, false) }
        wordListPresenter.attachView(viewState)
        wordListPresenter.logout(true)

        coVerify { wordRepository.clearDatabase() }
    }

    /*@Test
    fun `test open word detail screen`() {
        wordListPresenter.onItemClick(0)
        verify { router.navigateTo(Screens.wordDetailScreen(null)) }
    }*/

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}
