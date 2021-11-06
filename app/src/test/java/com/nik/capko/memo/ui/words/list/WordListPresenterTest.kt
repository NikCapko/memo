package com.nik.capko.memo.ui.words.list

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.nik.capko.memo.repository.WordRepository
import com.nik.capko.memo.ui.Screens
import com.nik.capko.memo.utils.AppStorage
import com.nik.capko.memo.utils.Constants
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class WordListPresenterTest {

    private val dispatcher = TestCoroutineDispatcher()

    @RelaxedMockK
    private lateinit var router: Router

    @RelaxedMockK
    private lateinit var appStorage: AppStorage

    @RelaxedMockK
    private lateinit var viewState: WordListView

    @MockK
    private lateinit var wordRepository: WordRepository

    private lateinit var wordListPresenter: WordListPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        wordListPresenter = WordListPresenter(router, wordRepository, appStorage)
    }

    @Test
    fun `test load data on first attach view to presenter`() = runBlockingTest {
        coEvery { wordRepository.getWordsFromDB() } returns emptyList()

        wordListPresenter.attachView(viewState)

        coVerify(exactly = 1) { viewState.startLoading() }
        coVerify(exactly = 1) { viewState.showWords(emptyList()) }
        coVerify(exactly = 1) { viewState.completeLoading() }
    }

    @Test
    fun `test enable sound on click on item`() = runBlockingTest {
        coEvery { wordRepository.getWordsFromDB() } returns emptyList()

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
    fun `test logout with clear databases`() = runBlockingTest {
        coEvery { wordRepository.clearDatabase() } just runs

        wordListPresenter.attachView(viewState)
        wordListPresenter.logout(true)

        verify { appStorage.put(Constants.IS_REGISTER, false) }
        coVerify { wordRepository.clearDatabase() }
    }

    @Test
    fun `test open word detail screen`() {
        val mockedWordDetailScreen = mockk<FragmentScreen>()

        mockkObject(Screens)
        every { Screens.wordDetailScreen(any()) } returns mockedWordDetailScreen

        wordListPresenter.onItemClick(0)
        verify { router.navigateTo(mockedWordDetailScreen) }
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}
