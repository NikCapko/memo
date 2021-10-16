package com.nik.capko.memo.ui.games.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Game
import com.nik.capko.memo.databinding.FragmentGamesBinding
import com.nik.capko.memo.ui.games.list.adapter.GamesAdapter
import com.nik.capko.memo.utils.extensions.lazyUnsafe
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class GamesFragment : MvpAppCompatFragment(), GamesMvpView {

    @Inject
    lateinit var presenterProvider: Provider<GamesPresenter>
    private val presenter: GamesPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentGamesBinding::bind)

    private val adapter: GamesAdapter by lazyUnsafe {
        GamesAdapter { position ->
            presenter.onItemClick(position)
        }
    }

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
        initAdapters()
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
