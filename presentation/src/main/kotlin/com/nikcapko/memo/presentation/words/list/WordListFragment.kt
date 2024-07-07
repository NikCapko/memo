package com.nikcapko.memo.presentation.words.list

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nikcapko.memo.core.common.Constants
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.extensions.observe
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.core.ui.view.ProgressView
import com.nikcapko.memo.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

private const val SPEECH_RATE = 0.6f

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class WordListFragment : BaseFragment(), ProgressView, WordListEventController {

    private val viewModel by viewModels<WordListViewModel>()

    private var tts: TextToSpeech? = null

    private val localBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Constants.LOAD_WORDS_EVENT) {
                viewModel.loadWords()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager
            .getInstance(requireContext())
            .registerReceiver(localBroadcastReceiver, IntentFilter(Constants.LOAD_WORDS_EVENT))
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
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initTextToSpeech()
        observe()
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
        }
        requireActivity().addMenuProvider(
            /* provider = */
            object : MenuProvider {
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
            /* owner = */ viewLifecycleOwner,
        )
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
        observe(viewModel.eventFlow) { it.apply(this) }
    }

    override fun showNeedMoreWordsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.attention)
            .setMessage(R.string.need_add_words)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showClearDatabaseDialog() {
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

    override fun speakOut(word: String?) {
        if (!word.isNullOrEmpty()) {
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
            Toast.makeText(context, word, Toast.LENGTH_SHORT).show()
        }
    }

    override fun startLoading() {
    }

    override fun errorLoading(errorMessage: String?) {
    }

    override fun completeLoading() {
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
