package com.nik.capko.memo.ui.games.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Game
import com.nik.capko.memo.data.GameType
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.FragmentGamesBinding
import com.nik.capko.memo.ui.games.find_pairs.FindPairsFragment
import com.nik.capko.memo.ui.games.list.adapter.GamesAdapter
import com.nik.capko.memo.ui.games.phrases.PhrasesFragment
import com.nik.capko.memo.ui.games.select_translate.SelectTranslateFragment
import com.nik.capko.memo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class GamesFragment @Inject constructor() : MvpAppCompatFragment(), GamesMvpView {

    @Inject
    lateinit var presenterProvider: Provider<GamesPresenter>
    private val presenter: GamesPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentGamesBinding::bind)

    private lateinit var adapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initAdapters()
    }

    private fun setListeners() {
        adapter = GamesAdapter { position ->
            presenter.onItemClick(position)
        }
    }

    private fun initAdapters() {
        viewBinding.rvGames.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GamesFragment.adapter
        }
    }

    override fun showGames(games: List<Game>) {
        adapter.games = games
    }

    override fun showGame(gameId: GameType, words: List<Word>) {
        when (gameId) {
            GameType.SELECT_TRANSLATE -> {
                val bundle = bundleOf(
                    SelectTranslateFragment.WORDS to words
                )
                (activity as? MainActivity)?.openFragment(
                    SelectTranslateFragment::class.java,
                    bundle
                )
            }
            GameType.FIND_PAIRS -> {
                val bundle = bundleOf(
                    FindPairsFragment.WORDS to words
                )
                (activity as? MainActivity)?.openFragment(FindPairsFragment::class.java, bundle)
            }
            GameType.PHRASES -> {
                val bundle = bundleOf(
                    PhrasesFragment.WORDS to words
                )
                (activity as? MainActivity)?.openFragment(PhrasesFragment::class.java, bundle)
            }
        }
    }
}
