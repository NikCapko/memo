package com.nikcapko.memo.ui.dictionary

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.core.viewmodel.DataSendingViewModelState
import com.nikcapko.memo.R
import com.nikcapko.memo.base.view.ProgressDialogView
import com.nikcapko.memo.base.view.ProgressMvpView
import com.nikcapko.memo.data.Dictionary
import com.nikcapko.memo.databinding.FragmentDictionaryBinding
import com.nikcapko.memo.ui.dictionary.adapter.DictionaryAdapter
import com.nikcapko.memo.utils.extensions.lazyUnsafe
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class DictionaryFragment : MvpAppCompatFragment(), ProgressMvpView, ProgressDialogView {

    private val viewModel by viewModels<DictionaryViewModel>()

    private val viewBinding by viewBinding(FragmentDictionaryBinding::bind)

    private val adapter: DictionaryAdapter by lazyUnsafe {
        DictionaryAdapter { position ->
            viewModel.onItemClick(position)
        }
    }

    private val proDialog: ProgressDialog by lazyUnsafe {
        ProgressDialog(context).apply {
            setTitle("Загрузка...")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        observe()
    }

    private fun initAdapters() {
        viewBinding.rvDictionary.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DictionaryFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun observe() {
        viewModel.dataLoadingViewModelState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DataLoadingViewModelState.LoadingState -> startLoading()
                DataLoadingViewModelState.NoItemsState -> showDictionaryList(emptyList())
                is DataLoadingViewModelState.LoadedState<*> -> {
                    val data = (state.data as? List<*>)?.filterIsInstance<Dictionary>()
                    showDictionaryList(data.orEmpty())
                    completeLoading()
                }
                is DataLoadingViewModelState.ErrorState -> errorLoading(state.exception.message)
            }
        }
        viewModel.dictionaryLoadingViewModelState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DataSendingViewModelState.SendingState -> startProgressDialog()
                DataSendingViewModelState.SentState -> completeProgressDialog()
                is DataSendingViewModelState.ErrorState -> errorProgressDialog(state.exception.message)
            }
        }
        viewModel.loadDictionaryDialogEvent.observe(viewLifecycleOwner) { loadDictionary ->
            loadDictionary.data?.let { showLoadingDialog(it) }
            loadDictionary.handled = true
        }
    }

    private fun showDictionaryList(dictionaryList: List<Dictionary>) {
        adapter.dictionaryList = dictionaryList
    }

    private fun showLoadingDialog(position: Int) {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.app_name)
            setMessage(R.string.dictionary_load_words)
            setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                viewModel.loadDictionary(position)
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    override fun startLoading() {
        with(viewBinding) {
            pvLoad.startLoading()
            rvDictionary.makeVisible()
        }
    }

    override fun errorLoading(errorMessage: String?) {
        with(viewBinding) {
            pvLoad.errorLoading(errorMessage)
            rvDictionary.makeGone()
        }
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.completeLoading()
            rvDictionary.makeVisible()
        }
    }

    override fun onRetry() {
        viewModel.loadDictionaryList()
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
