package com.nikcapko.memo.presentation.navigation

import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.presentation.games.findpairs.FindPairsFragment
import com.nikcapko.memo.presentation.games.list.GamesFragment
import com.nikcapko.memo.presentation.games.selecttranslate.SelectTranslateFragment
import com.nikcapko.memo.presentation.words.details.ui.WordDetailsFragment
import com.nikcapko.memo.presentation.words.list.ui.WordListFragment
import javax.inject.Inject

class RootNavigatorImpl @Inject constructor(
    private val router: Router,
) : RootNavigator {

    override fun pushWordListScreen() {
        router.navigateTo(
            screen = FragmentScreen { WordListFragment() },
        )
    }

    override fun pushWordDetailScreen(word: WordModel?) {
        router.navigateTo(
            screen = FragmentScreen {
                WordDetailsFragment().apply {
                    arguments = bundleOf(
                        WordDetailsFragment.WORD_ARGUMENT to word,
                    )
                }
            },
        )
    }

    override fun pushGamesScreen() {
        router.navigateTo(
            screen = FragmentScreen { GamesFragment() },
        )
    }

    override fun pushSelectTranslateScreen() {
        router.navigateTo(
            screen = FragmentScreen { SelectTranslateFragment() },
        )
    }

    override fun pushFindPairsScreen() {
        router.navigateTo(
            screen = FragmentScreen { FindPairsFragment() },
        )
    }

    override fun back() {
        router.exit()
    }
}
