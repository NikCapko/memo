package com.nikcapko.memo.ui.words.list

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.memo.R
import com.nikcapko.memo.base.ui.BaseFragment
import com.nikcapko.memo.base.view.ProgressMvpView
import com.nikcapko.memo.data.Game
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.databinding.FragmentWordListBinding
import com.nikcapko.memo.ui.words.list.adapter.WordListAdapter
import com.nikcapko.memo.utils.AppStorage
import com.nikcapko.memo.utils.Constants
import com.nikcapko.memo.utils.extensions.lazyUnsafe
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.ktx.moxyPresenter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class WordListFragment : BaseFragment(), WordListView, ProgressMvpView {

    @Inject
    lateinit var appStorage: AppStorage

    @Inject
    lateinit var presenterProvider: Provider<WordListPresenter>
    private val presenter: WordListPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentWordListBinding::bind)

    private var gameMenuItem: MenuItem? = null

    private var tts: TextToSpeech? = null

    private val adapter: WordListAdapter by lazyUnsafe {
        WordListAdapter(
            onItemClick = { position -> presenter.onItemClick(position) },
            onEnableSound = { position -> presenter.onEnableSound(position) }
        )
    }

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
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localBroadcastReceiver, IntentFilter(Constants.LOAD_WORDS_EVENT))
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_action, menu)
        val logoutMenuItem = menu.findItem(R.id.action_logout)
        logoutMenuItem.isVisible = appStorage.get(Constants.IS_REGISTER, false)
        gameMenuItem = menu.findItem(R.id.action_games)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setListeners()
        initAdapters()
        initTextToSpeech()
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
        }
    }

    private fun setListeners() {
        viewBinding.apply {
            btnAddWord.setOnClickListener { presenter.onAddWordClick() }
            pvLoad.onRetryClick = { onRetry() }
        }
    }

    private fun initAdapters() {
        viewBinding.rvWords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WordListFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initTextToSpeech() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = tts?.setLanguage(Locale.US) ?: TextToSpeech.LANG_NOT_SUPPORTED
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "This Language is not supported")
                }
            } else {
                Log.e("TTS", "Initialization Failed!")
            }
        }
        tts?.setSpeechRate(SPEECH_RATE)
    }

    override fun showWords(wordsList: List<Word>) {
        gameMenuItem?.isVisible = !wordsList.isNullOrEmpty() && wordsList.size >= Game.MAX_WORDS_COUNT_SELECT_TRANSLATE
        adapter.words = wordsList
    }

    override fun showClearDatabaseDialog() {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.attention)
            setMessage(R.string.clear_database)
            setCancelable(true)
            setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                presenter.logout(true)
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
                presenter.logout(false)
            }
        }.create().show()
    }

    override fun speakOut(word: String?) {
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
        Toast.makeText(context, word, Toast.LENGTH_SHORT).show()
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
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.completeLoading()
            rvWords.makeVisible()
            btnAddWord.makeVisible()
        }
    }

    override fun onRetry() {
        presenter.loadWords()
    }

    override fun onPause() {
        tts?.stop()
        super.onPause()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(localBroadcastReceiver)
        super.onDestroy()
    }

    companion object {
        const val SPEECH_RATE = 0.6f
    }
}
