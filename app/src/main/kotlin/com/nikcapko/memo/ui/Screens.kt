package com.nikcapko.memo.ui

import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.ui.games.find_pairs.FindPairsFragment
import com.nikcapko.memo.ui.games.list.GamesFragment
import com.nikcapko.memo.ui.games.select_translate.SelectTranslateFragment
import com.nikcapko.memo.ui.words.detail.WordDetailFragment
import com.nikcapko.memo.ui.words.list.WordListFragment

object Screens {

    fun wordListScreen() = FragmentScreen {
        WordListFragment()
    }

    fun wordDetailScreen(word: Word?) = FragmentScreen {
        WordDetailFragment().apply {
            arguments = bundleOf(
                WordDetailFragment.WORD to word,
            )
        }
    }

    fun gamesScreen() = FragmentScreen {
        GamesFragment()
    }

    fun selectTranslateScreen() = FragmentScreen {
        SelectTranslateFragment()
    }

    fun findPairsScreen() = FragmentScreen {
        FindPairsFragment()
    }
}
