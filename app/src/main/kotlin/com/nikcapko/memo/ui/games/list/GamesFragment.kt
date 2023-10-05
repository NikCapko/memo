package com.nikcapko.memo.ui.games.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.memo.R
import com.nikcapko.memo.base.ui.BaseFragment
import com.nikcapko.memo.databinding.FragmentGamesBinding
import com.nikcapko.memo.ui.games.list.adapter.GamesAdapter
import com.nikcapko.memo.utils.extensions.lazyAndroid
import com.nikcapko.memo.utils.extensions.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesFragment : BaseFragment() {

    private val viewModel by viewModels<GamesViewModel>()

    private val viewBinding by viewBinding(FragmentGamesBinding::bind)

    private val adapter: GamesAdapter by lazyAndroid {
        GamesAdapter { position ->
            viewModel.onItemClick(position)
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
        observe()
    }

    private fun initAdapters() {
        viewBinding.rvGames.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GamesFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun observe() {
        observeFlow(viewModel.state) {
            adapter.games = it
        }
    }
}
