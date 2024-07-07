package com.nikcapko.memo.presentation.ui.games.list

import com.nikcapko.domain.model.Game
import com.nikcapko.memo.presentation.navigation.RootNavigator
import com.nikcapko.memo.presentation.domain.GamesInteractor
import com.nikcapko.memo.presentation.games.list.GamesFlowWrapper
import com.nikcapko.memo.presentation.games.list.GamesViewModel
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class GamesViewModelTest : FunSpec({

    val gamesInteractor = mockk<GamesInteractor>(relaxed = true)
    val gamesFlowWrapper = mockk<GamesFlowWrapper>(relaxed = true)
    val rootNavigator = mockk<RootNavigator>(relaxed = true)

    val gameList = listOf(
        Game(Game.Type.SELECT_TRANSLATE, Game.Type.SELECT_TRANSLATE.value),
        Game(Game.Type.FIND_PAIRS, Game.Type.FIND_PAIRS.value),
    )

    fun createViewModel() = GamesViewModel(
        gamesInteractor = gamesInteractor,
        gamesFlowWrapper = gamesFlowWrapper,
        rootNavigator = rootNavigator,
    )

    test("check load games list") {
        coEvery { gamesInteractor.getDefaultGamesList() } returns gameList

        val viewModel = createViewModel()

        verify { gamesFlowWrapper.update(gameList) }
    }

    test("check click on SELECT_TRANSLATE games item") {
        every { gamesFlowWrapper.value() } returns gameList

        val viewModel = createViewModel()

        viewModel.onItemClick(0)

        verify { rootNavigator.pushSelectTranslateScreen() }
    }

    test("check click on FIND_PAIRS games item") {
        every { gamesFlowWrapper.value() } returns gameList

        val viewModel = createViewModel()

        viewModel.onItemClick(1)

        verify { rootNavigator.pushFindPairsScreen() }
    }


})
