package com.nikcapko.memo.ui.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.InstantExecutorExtension
import com.nikcapko.memo.domain.GamesInteractor
import com.nikcapko.memo.navigation.Navigator
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Test for [GamesViewModel]
 */
@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class GamesViewModelTest {

    private val gamesInteractor = mockk<GamesInteractor>(relaxed = true)
    private val gamesFlowWrapper = mockk<GamesFlowWrapper>(relaxed = true)
    private val navigator = spyk<Navigator>()

    private lateinit var viewModel: GamesViewModel

    private val gameList = listOf(
        Game(Game.Type.SELECT_TRANSLATE, Game.Type.SELECT_TRANSLATE.value),
        Game(Game.Type.FIND_PAIRS, Game.Type.FIND_PAIRS.value),
    )

    @Test
    fun `check load games list`() = runTest {
        coEvery { gamesInteractor.getDefaultGamesList() } returns gameList

        viewModel = createViewModel()
        verify { gamesFlowWrapper.update(gameList) }
    }

    @Test
    fun `check click on SELECT_TRANSLATE games item`() = runTest {
        every { gamesFlowWrapper.value() } returns gameList

        viewModel = createViewModel()
        viewModel.onItemClick(0)

        verify { navigator.pushSelectTranslateScreen() }
    }

    @Test
    fun `check click on FIND_PAIRS games item`() = runTest {
        every { gamesFlowWrapper.value() } returns gameList

        viewModel = createViewModel()
        viewModel.onItemClick(1)

        verify { navigator.pushFindPairsScreen() }
    }

    private fun createViewModel() = GamesViewModel(
        gamesInteractor = gamesInteractor,
        gamesFlowWrapper = gamesFlowWrapper,
        navigator = navigator,
    )
}
