@file:Suppress("ClassOrdering")

package com.nik.capko.memo.ui.words.detail

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
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.FragmentWordDetailBinding
import com.nik.capko.memo.utils.Constants
import com.nik.capko.memo.utils.extensions.argument
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class WordDetailFragment : MvpAppCompatFragment(), WordDetailMvpView {

    companion object {
        const val WORD = "word"
    }

    @Inject
    lateinit var presenterProvider: Provider<WordDetailPresenter>
    private val presenter: WordDetailPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentWordDetailBinding::bind)

    private var word: Word? by argument()

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
            btnSave.setOnClickListener {
                presenter.onSaveWord(
                    etWord.text.toString(),
                    etTranslate.text.toString()
                )
            }
            btnDelete.setOnClickListener { presenter.onDeleteWord() }
        }
    }

    override fun initView(word: Word) {
        viewBinding.etWord.setText(word.word)
        viewBinding.etTranslate.setText(word.translation)
    }

    override fun sendSuccessResult() {
        val localIntent = Intent(Constants.LOAD_WORDS_EVENT)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntent)
        onCloseScreen()
    }

    override fun onCloseScreen() {
        activity?.onBackPressed()
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
