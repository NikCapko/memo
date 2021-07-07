package com.nik.capko.memo.ui

import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.ui.dictionary.DictionaryFragment
import com.nik.capko.memo.ui.games.find_pairs.FindPairsFragment
import com.nik.capko.memo.ui.games.list.GamesFragment
import com.nik.capko.memo.ui.games.phrases.PhrasesFragment
import com.nik.capko.memo.ui.games.select_translate.SelectTranslateFragment
import com.nik.capko.memo.ui.sign_in.SignInFragment
import com.nik.capko.memo.ui.words.detail.WordDetailFragment
import com.nik.capko.memo.ui.words.list.WordListFragment

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

    fun signInScreen() = FragmentScreen {
        SignInFragment()
    }

    fun dictionaryScreen() = FragmentScreen {
        DictionaryFragment()
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

    fun phrasesScreen() = FragmentScreen {
        PhrasesFragment()
    }
}
