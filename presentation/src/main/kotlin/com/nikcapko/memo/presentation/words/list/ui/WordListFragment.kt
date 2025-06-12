package com.nikcapko.memo.presentation.words.list.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import com.nikcapko.memo.core.common.Constants
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.extensions.observe
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.core.ui.viewmodel.lazyViewModels
import com.nikcapko.memo.presentation.R
import com.nikcapko.memo.presentation.words.list.WordListEvent
import com.nikcapko.memo.presentation.words.list.WordListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

private const val SPEECH_RATE = 0.6f
private const val PITCH = 1.0f

@AndroidEntryPoint
internal class WordListFragment : BaseFragment() {

    private val viewModel by lazyViewModels<WordListViewModel>()

    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerFragmentResult()
    }

    private fun registerFragmentResult() {
        parentFragmentManager.setFragmentResultListener(Constants.LOAD_WORDS_EVENT, this) { _, _ ->
            viewModel.loadWords()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ComposeView = ComposeView(requireContext()).apply {
        setContent {
            ComposeTheme {
                WordListScreen(
                    state = viewModel.state.collectAsState(null),
                    onItemClick = { viewModel.onItemClick(it) },
                    onSpeakClick = { viewModel.onEnableSound(it) },
                    addItemClick = { viewModel.onAddWordClick() },
                    onGamesClick = { viewModel.openGamesScreen() },
                    onClearDatabaseClick = { viewModel.onClearDatabaseClick() },
                    onRetryClick = { viewModel.onRetryClick() },
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTextToSpeech()
        observe(viewModel.eventFlow, ::observeEvents)
    }

    private fun observeEvents(event: WordListEvent) = when (event) {
        is WordListEvent.ShowClearDatabaseEvent -> showClearDatabaseDialog()
        is WordListEvent.ShowNeedMoreWordsEvent -> showNeedMoreWordsDialog()
        is WordListEvent.SpeakOutEvent -> speakOut(event.word)
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
        tts?.setPitch(PITCH)
        tts?.setSpeechRate(SPEECH_RATE)
    }

    private fun showNeedMoreWordsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.attention)
            .setMessage(R.string.need_add_words)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showClearDatabaseDialog() {
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

    private fun speakOut(word: String?) {
        if (!word.isNullOrEmpty()) {
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
            Toast.makeText(context, word, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        tts?.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        tts?.shutdown()
        tts = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        parentFragmentManager.clearFragmentResult(Constants.LOAD_WORDS_EVENT)
        super.onDestroy()
    }
}
