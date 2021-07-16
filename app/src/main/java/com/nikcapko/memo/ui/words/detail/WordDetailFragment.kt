@file:Suppress("ClassOrdering")

package com.nikcapko.memo.ui.words.detail

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nikcapko.memo.R
import com.nikcapko.memo.base.ui.BaseFragment
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.databinding.FragmentWordDetailBinding
import com.nikcapko.memo.utils.Constants
import com.nikcapko.memo.utils.extensions.argument
import com.nikcapko.memo.utils.extensions.hideKeyboard
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class WordDetailFragment : BaseFragment(), WordDetailView {

    companion object {
        const val WORD = "WordDetailFragment.WORD"
    }

    @Inject
    lateinit var presenterProvider: Provider<WordDetailPresenter>
    private val presenter: WordDetailPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentWordDetailBinding::bind)

    private val word by argument<Word>(WORD)

    private val proDialog: ProgressDialog by lazy {
        ProgressDialog(context).apply {
            setTitle("Загрузка...")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getArgs()
    }

    private fun getArgs() {
        presenter.setArguments(word)
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
        return inflater.inflate(R.layout.fragment_word_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(viewBinding) {
            btnAdd.setOnClickListener {
                hideKeyboard()
                presenter.onSaveWord(
                    etWord.text.toString(),
                    etTranslate.text.toString()
                )
            }
            btnSave.setOnClickListener {
                hideKeyboard()
                presenter.onSaveWord(
                    etWord.text.toString(),
                    etTranslate.text.toString()
                )
            }
            btnDelete.setOnClickListener {
                hideKeyboard()
                presenter.onDeleteWord()
            }
            presenter.setValidationFields(
                RxTextView.textChanges(etWord),
                RxTextView.textChanges(etTranslate),
            )
        }
    }

    override fun initView(word: Word?) {
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

    override fun sendSuccessResult() {
        val localIntent = Intent(Constants.LOAD_WORDS_EVENT)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntent)
    }

    override fun enableSaveButton(enable: Boolean) {
        viewBinding.btnAdd.isEnabled = enable
        viewBinding.btnSave.isEnabled = enable
    }

    override fun startProgressDialog() {
        proDialog.show()
    }

    override fun errorProgressDialog(errorMessage: String?) {
        proDialog.dismiss()
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.app_name)
            setMessage(errorMessage)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    override fun completeProgressDialog() {
        proDialog.dismiss()
    }
}
