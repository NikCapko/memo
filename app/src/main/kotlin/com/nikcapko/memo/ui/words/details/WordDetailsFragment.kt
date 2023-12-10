package com.nikcapko.memo.ui.words.details

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.memo.R
import com.nikcapko.memo.base.ui.BaseFragment
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.databinding.FragmentWordDetailBinding
import com.nikcapko.memo.utils.Constants
import com.nikcapko.memo.utils.extensions.androidLazy
import com.nikcapko.memo.utils.extensions.argument
import com.nikcapko.memo.utils.extensions.hideKeyboard
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeVisible
import com.nikcapko.memo.utils.extensions.observe
import dagger.hilt.android.AndroidEntryPoint

internal const val WORD_ARGUMENT = "WordDetailFragment.WORD"

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class WordDetailsFragment : BaseFragment() {

    private val viewBinding by viewBinding(FragmentWordDetailBinding::bind)
    private val viewModel by viewModels<WordDetailsViewModel>()

    private val stateWrapper: WordDetailsFlowWrapper by androidLazy { viewModel }
    private val viewController: WordDetailsViewController by androidLazy { viewModel }

    private val word by argument<Word>(WORD_ARGUMENT)

    private val progressDialog: ProgressDialog by androidLazy {
        ProgressDialog(requireContext()).apply {
            setTitle(getString(R.string.loading))
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    private fun initObservers() {
        observe(stateWrapper.wordState) { initView(it) }
        observe(stateWrapper.progressLoadingState) { showProgressDialog ->
            if (showProgressDialog) {
                startProgressDialog()
            } else {
                completeProgressDialog()
            }
        }
        observe(stateWrapper.enableSaveButtonState) { enableSaveButton(it) }
        observe(stateWrapper.eventFlow) { event ->
            when (event) {
                is WordDetailsEvent.CloseScreenEvent -> sendSuccessResult()
            }
        }
    }

    private fun getArgs() {
        viewController.setArguments(word)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_word_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setListeners()
        initObservers()
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setListeners() {
        with(viewBinding) {
            btnAdd.setOnClickListener {
                hideKeyboard()
                viewController.onSaveWord(
                    etWord.text.toString(),
                    etTranslate.text.toString(),
                )
            }
            btnSave.setOnClickListener {
                hideKeyboard()
                viewController.onSaveWord(
                    etWord.text.toString(),
                    etTranslate.text.toString(),
                )
            }
            btnDelete.setOnClickListener {
                hideKeyboard()
                viewController.onDeleteWord()
            }

            etWord.addTextChangedListener {
                viewController.changeWordField(it.toString())
            }
            etTranslate.addTextChangedListener {
                viewController.changeTranslateField(it.toString())
            }
        }
    }

    private fun initView(word: Word?) {
        with(viewBinding) {
            word?.let {
                flAddWordContainer.makeGone()
                llEditWordContainer.makeVisible()
                etWord.setText(it.word)
                etTranslate.setText(it.translation)
                etFrequency.setText(it.frequency.toString())
            } ?: run {
                flAddWordContainer.makeVisible()
                llEditWordContainer.makeGone()
                tilFrequency.makeGone()
            }
        }
    }

    private fun sendSuccessResult() {
        val localIntent = Intent(Constants.LOAD_WORDS_EVENT)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntent)
    }

    private fun enableSaveButton(enable: Boolean) = with(viewBinding) {
        btnAdd.isEnabled = enable
        btnSave.isEnabled = enable
    }

    private fun startProgressDialog() {
        progressDialog.show()
    }

    private fun completeProgressDialog() {
        progressDialog.dismiss()
    }
}
