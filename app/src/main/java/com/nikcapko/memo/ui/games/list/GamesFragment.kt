package com.nikcapko.memo.ui.games.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.memo.R
import com.nikcapko.memo.data.Game
import com.nikcapko.memo.databinding.FragmentGamesBinding
import com.nikcapko.memo.ui.games.list.adapter.GamesAdapter
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
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun showGames(games: List<Game>) {
        adapter.games = games
    }
}
