package com.nikcapko.memo.navigation

import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.ui.games.find_pairs.FindPairsFragment
import com.nikcapko.memo.ui.games.list.GamesFragment
import com.nikcapko.memo.ui.games.select_translate.SelectTranslateFragment
import com.nikcapko.memo.ui.words.detail.WordDetailFragment
import com.nikcapko.memo.ui.words.list.WordListFragment
import javax.inject.Inject

internal class NavigatorImpl @Inject constructor(
    private val router: Router,
) : Navigator {

    override fun pushWordListScreen() {
        router.navigateTo(
            screen = FragmentScreen { WordListFragment() }
        )
    }

    override fun pushWordDetailScreen(word: Word?) {
        router.navigateTo(
            screen = FragmentScreen {
                WordDetailFragment().apply {
                    arguments = bundleOf(
                        WordDetailFragment.WORD to word,
                    )
                }
            }
        )
    }

    override fun pushGamesScreen() {
        router.navigateTo(
            screen = FragmentScreen { GamesFragment() }
        )
    }

    override fun pushSelectTranslateScreen() {
        router.navigateTo(
            screen = FragmentScreen { SelectTranslateFragment() }
        )
    }

    override fun pushFindPairsScreen() {
        router.navigateTo(
            screen = FragmentScreen { FindPairsFragment() }
        )
    }
}