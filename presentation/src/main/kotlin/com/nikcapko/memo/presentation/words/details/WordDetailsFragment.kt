package com.nikcapko.memo.presentation.words.details

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.memo.core.common.Constants
import com.nikcapko.memo.core.common.androidLazy
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.ui.BaseFragment
import com.nikcapko.memo.core.ui.extensions.argument
import com.nikcapko.memo.core.ui.extensions.hideKeyboard
import com.nikcapko.memo.core.ui.extensions.makeGone
import com.nikcapko.memo.core.ui.extensions.makeVisible
import com.nikcapko.memo.core.ui.extensions.observe
import com.nikcapko.memo.core.ui.viewmodel.lazyViewModels
import com.nikcapko.memo.presentation.R
import com.nikcapko.memo.presentation.databinding.FragmentWordDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

internal const val WORD_ARGUMENT = "WordDetailFragment.WORD"

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class WordDetailsFragment : BaseFragment(), WordDetailsEventController {

    private val viewBinding by viewBinding(FragmentWordDetailBinding::bind)

    private val word by argument<Word>(WORD_ARGUMENT)

    private val viewModel by lazyViewModels<WordDetailsViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras
                .withCreationCallback<WordDetailsViewModel.Factory> { factory ->
                    factory.create(word)
                }
        }
    )

    private val progressDialog: ProgressDialog by androidLazy {
        ProgressDialog(requireContext()).apply {
            setTitle(getString(R.string.loading))
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    private fun initObservers() {
        observe(viewModel.wordState) { initView(it) }
        observe(viewModel.progressLoadingState) { showProgressDialog ->
            if (showProgressDialog) {
                startProgressDialog()
            } else {
                completeProgressDialog()
            }
        }
        observe(viewModel.enableSaveButtonState) { enableSaveButton(it) }
        observe(viewModel.eventFlow) { event ->
            event.apply(this)
        }
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

    override fun sendSuccessResult() {
        parentFragmentManager.setFragmentResult(Constants.LOAD_WORDS_EVENT, bundleOf())
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
                viewModel.processCommand(
                    command = WordDetailsCommand.SaveWordCommand(
                        word = etWord.text.toString(),
                        translate = etTranslate.text.toString(),
                    )
                )
            }
            btnSave.setOnClickListener {
                hideKeyboard()
                viewModel.processCommand(
                    command = WordDetailsCommand.SaveWordCommand(
                        word = etWord.text.toString(),
                        translate = etTranslate.text.toString(),
                    )
                )
            }
            btnDelete.setOnClickListener {
                hideKeyboard()
                viewModel.processCommand(WordDetailsCommand.DeleteWordCommand)
            }

            etWord.addTextChangedListener {
                viewModel.processCommand(WordDetailsCommand.ChangeWordFieldCommand(it.toString()))
            }
            etTranslate.addTextChangedListener {
                viewModel.processCommand(WordDetailsCommand.ChangeTranslateFieldCommand(it.toString()))
            }
        }
    }

    private fun initView(word: Word?) = with(viewBinding) {
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
