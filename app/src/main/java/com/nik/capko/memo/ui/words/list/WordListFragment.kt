package com.nik.capko.memo.ui.words.list

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.app.appStorage
import com.nik.capko.memo.base.ui.BaseFragment
import com.nik.capko.memo.data.Game
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.FragmentWordListBinding
import com.nik.capko.memo.ui.words.list.adapter.WordListAdapter
import com.nik.capko.memo.utils.Constants
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.ktx.moxyPresenter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class WordListFragment @Inject constructor() :
    BaseFragment(),
    WordListView,
    TextToSpeech.OnInitListener {

    @Suppress("ClassOrdering")
    companion object {
        const val SPEECH_RATE = 0.6f
    }

    @Inject
    lateinit var presenterProvider: Provider<WordListPresenter>
    private val presenter: WordListPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentWordListBinding::bind)

    private lateinit var adapter: WordListAdapter

    private var gameMenuItem: MenuItem? = null

    private var tts: TextToSpeech? = null

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
        tts = TextToSpeech(context, this)
        tts?.setSpeechRate(SPEECH_RATE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_action, menu)
        val logoutMenuItem = menu.findItem(R.id.action_logout)
        logoutMenuItem.isVisible = appStorage.get(Constants.IS_REGISTER, false)
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
        adapter = WordListAdapter(
            { position ->
                presenter.onItemClick(position)
            },
            { position ->
                presenter.onEnableSound(position)
            }
        )
    }

    private fun initAdapters() {
        viewBinding.rvWords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WordListFragment.adapter
        }
    }

    override fun showWords(wordsList: List<Word>) {
        gameMenuItem?.isVisible =
            wordsList.isNotEmpty() && wordsList.size >= Game.MAX_WORDS_COUNT_SELECT_TRANSLATE
        adapter.words = wordsList
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result: Int = tts?.setLanguage(Locale.US) ?: TextToSpeech.LANG_NOT_SUPPORTED
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported")
            }
        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    override fun speakOut(word: String?) {
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null)
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
