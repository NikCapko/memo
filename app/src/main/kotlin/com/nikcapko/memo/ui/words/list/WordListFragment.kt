package com.nikcapko.memo.ui.words.list

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.R
import com.nikcapko.memo.base.ui.BaseFragment
import com.nikcapko.memo.base.view.ProgressView
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.databinding.FragmentWordListBinding
import com.nikcapko.memo.ui.words.list.adapter.WordListAdapter
import com.nikcapko.memo.utils.Constants
import com.nikcapko.memo.utils.extensions.lazyAndroid
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeVisible
import com.nikcapko.memo.utils.extensions.observe
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


private const val SPEECH_RATE = 0.6f

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class WordListFragment : BaseFragment(), ProgressView {

    private val viewModel by viewModels<WordListViewModel>()
    private val viewBinding by viewBinding(FragmentWordListBinding::bind)

    private var tts: TextToSpeech? = null

    private val adapter: WordListAdapter by lazyAndroid {
        WordListAdapter(
            onItemClick = viewModel::onItemClick,
            onEnableSound = viewModel::onEnableSound,
        )
    }

    private val localBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Constants.LOAD_WORDS_EVENT) {
                viewModel.loadWords()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localBroadcastReceiver, IntentFilter(Constants.LOAD_WORDS_EVENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setListeners()
        initAdapters()
        initTextToSpeech()
        observe()
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
        }
        requireActivity().addMenuProvider(
            /* provider = */ object : MenuProvider {
                override fun onPrepareMenu(menu: Menu) = Unit
                override fun onMenuClosed(menu: Menu) = Unit

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_action, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.action_games -> viewModel.openGamesScreen()
                        R.id.action_clear -> viewModel.onClearDatabaseClick()
                    }
                    return false
                }
            },
            /* owner = */ viewLifecycleOwner
        )
    }

    private fun setListeners() = with(viewBinding) {
        btnAddWord.setOnClickListener { viewModel.onAddWordClick() }
        pvLoad.onRetryClick = ::onRetry
    }

    private fun initAdapters() = with(viewBinding) {
        rvWords.apply {
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

    private fun observe() {
        observe(viewModel.state) { state ->
            when (state) {
                DataLoadingViewModelState.LoadingState -> startLoading()
                DataLoadingViewModelState.NoItemsState -> showWords(emptyList())
                is DataLoadingViewModelState.LoadedState<*> -> {
                    val data = (state.data as? List<*>)?.filterIsInstance<Word>()
                    showWords(data)
                    completeLoading()
                }

                is DataLoadingViewModelState.ErrorState -> Unit
            }
        }
        observe(viewModel.speakOutEvent) { speakOut(it.data) }
        observe(viewModel.showClearDatabaseDialog) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.attention)
                .setMessage(R.string.clear_database)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    viewModel.clearDatabase()
                }
                .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
                .create()
                .show()
        }
        observe(viewModel.showNeedMoreWordsDialog) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.attention)
                .setMessage(R.string.need_add_words)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun showWords(wordsList: List<Word>?) {
        adapter.words = wordsList
    }

    private fun speakOut(word: String?) {
        if (!word.isNullOrEmpty()) {
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
            Toast.makeText(context, word, Toast.LENGTH_SHORT).show()
        }
    }

    override fun startLoading() = with(viewBinding) {
        pvLoad.startLoading()
        rvWords.makeGone()
        btnAddWord.makeGone()
    }

    override fun errorLoading(errorMessage: String?) = with(viewBinding) {
        pvLoad.errorLoading(errorMessage)
        rvWords.makeGone()
        btnAddWord.makeGone()
    }

    override fun completeLoading() = with(viewBinding) {
        pvLoad.completeLoading()
        rvWords.makeVisible()
        btnAddWord.makeVisible()
    }

    override fun onRetry() {
        viewModel.loadWords()
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
}
