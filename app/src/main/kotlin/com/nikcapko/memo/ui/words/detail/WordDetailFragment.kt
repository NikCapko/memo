package com.nikcapko.memo.ui.words.detail

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
import com.nikcapko.memo.utils.extensions.argument
import com.nikcapko.memo.utils.extensions.hideKeyboard
import com.nikcapko.memo.utils.extensions.lazyUnsafe
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeVisible
import com.nikcapko.memo.utils.extensions.observeFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class WordDetailFragment : BaseFragment() {

    private val viewModel by viewModels<WordDetailViewModel>()

    private val viewBinding by viewBinding(FragmentWordDetailBinding::bind)

    private val word by argument<Word>(WORD)

    private val progressDialog: ProgressDialog by lazyUnsafe {
        ProgressDialog(context).apply {
            setTitle("Загрузка...")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    private fun initObservers() {
        observeFlow(viewModel.wordState) { word ->
            initView(word)
        }
        observeFlow(viewModel.progressLoadingState) { showProgressDialog ->
            if (showProgressDialog) {
                startProgressDialog()
            } else {
                completeProgressDialog()
            }
        }
        observeFlow(viewModel.enableSaveButtonState) {
            enableSaveButton(it)
        }
        observeFlow(viewModel.successResultChannel.receiveAsFlow()) {
            sendSuccessResult()
        }
    }

    private fun getArgs() {
        viewModel.setArguments(word)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
                viewModel.onSaveWord(
                    etWord.text.toString(),
                    etTranslate.text.toString()
                )
            }
            btnSave.setOnClickListener {
                hideKeyboard()
                viewModel.onSaveWord(
                    etWord.text.toString(),
                    etTranslate.text.toString()
                )
            }
            btnDelete.setOnClickListener {
                hideKeyboard()
                viewModel.onDeleteWord()
            }

            etWord.addTextChangedListener {
                viewModel.changeWordField(it.toString())
            }
            etTranslate.addTextChangedListener {
                viewModel.changeTranslateField(it.toString())
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

    private fun enableSaveButton(enable: Boolean) {
        viewBinding.btnAdd.isEnabled = enable
        viewBinding.btnSave.isEnabled = enable
    }

    private fun startProgressDialog() {
        progressDialog.show()
    }

    private fun completeProgressDialog() {
        progressDialog.dismiss()
    }

    companion object {
        const val WORD = "WordDetailFragment.WORD"
    }
}
