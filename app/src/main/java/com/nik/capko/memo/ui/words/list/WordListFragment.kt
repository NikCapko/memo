package com.nik.capko.memo.ui.words.list

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.app.appStorage
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.FragmentWordListBinding
import com.nik.capko.memo.ui.base.MainActivity
import com.nik.capko.memo.ui.dictionary.DictionaryFragment
import com.nik.capko.memo.ui.games.list.GamesFragment
import com.nik.capko.memo.ui.sign_in.SignInFragment
import com.nik.capko.memo.ui.words.detail.WordDetailFragment
import com.nik.capko.memo.ui.words.list.adapter.WordListAdapter
import com.nik.capko.memo.utils.Constants
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class WordListFragment @Inject constructor() : MvpAppCompatFragment(), WordListView {

    @Inject
    lateinit var presenterProvider: Provider<WordListPresenter>
    private val presenter: WordListPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentWordListBinding::bind)

    private lateinit var adapter: WordListAdapter

    private var gameMenuItem: MenuItem? = null

    private val localBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Constants.LOAD_WORDS_EVENT -> {
                    presenter.loadWords()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_action, menu)
        val logoutMenuItem = menu.findItem(R.id.action_logout)
        if (appStorage.get(Constants.IS_REGISTER, false)) {
            logoutMenuItem.isVisible = false
        }
        gameMenuItem = menu.findItem(R.id.action_games)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(Constants.LOAD_WORDS_EVENT)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(localBroadcastReceiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_games -> {
                presenter.openGamesScreen()
                true
            }
            R.id.action_dictionary -> {
                presenter.openDictionaryScreen()
                true
            }
            R.id.action_logout -> {
                presenter.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initAdapters()
    }

    private fun setListeners() {
        viewBinding.btnAddWord.setOnClickListener { presenter.onAddWordClick() }
        adapter = WordListAdapter { position ->
            presenter.onItemClick(position)
        }
    }

    private fun initAdapters() {
        viewBinding.rvWords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WordListFragment.adapter
        }
    }

    override fun showWords(wordsList: List<Word>) {
        gameMenuItem?.isVisible = wordsList.isNotEmpty()
        adapter.words = wordsList
    }

    override fun showWordDetail(word: Word?) {
        val bundle = bundleOf(
            WordDetailFragment.WORD to word,
        )
        (activity as? MainActivity)?.openFragment(WordDetailFragment::class.java, bundle)
    }

    override fun showAddWordScreen() {
        (activity as? MainActivity)?.openFragment(WordDetailFragment::class.java)
    }

    override fun showClearDatabaseDialog() {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.attention)
            setMessage(R.string.clear_database)
            setCancelable(true)
            setPositiveButton(
                R.string.yes
            ) { dialog, _ ->
                dialog.dismiss()
                presenter.logout(true)
            }
            setNegativeButton(
                R.string.no
            ) { dialog, _ ->
                dialog.dismiss()
                presenter.logout(false)
            }
        }.create().show()
    }

    override fun showGamesScreen() {
        (activity as? MainActivity)?.openFragment(GamesFragment::class.java)
    }

    override fun showDictionaryScreen() {
        (activity as? MainActivity)?.openFragment(DictionaryFragment::class.java)
    }

    override fun openSignInScreen() {
        (activity as? MainActivity)?.openFragment(SignInFragment::class.java)
    }

    override fun startLoading() {
        with(viewBinding) {
            pvLoad.startLoading()
            rvWords.makeGone()
            btnAddWord.makeGone()
        }
    }

    override fun errorLoading(errorMessage: String?) {
        with(viewBinding) {
            pvLoad.errorLoading(errorMessage)
            rvWords.makeGone()
            btnAddWord.makeGone()
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.completeLoading()
            rvWords.makeVisible()
            btnAddWord.makeVisible()
        }
    }
}
