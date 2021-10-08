package com.nik.capko.memo.ui.dictionary

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.databinding.FragmentDictionaryBinding
import com.nik.capko.memo.ui.dictionary.adapter.DictionaryAdapter
import com.nik.capko.memo.utils.Constants
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@Suppress("TooManyFunctions")
@AndroidEntryPoint
class DictionaryFragment : MvpAppCompatFragment(), DictionaryView {

    @Inject
    lateinit var presenterProvider: Provider<DictionaryPresenter>
    private val presenter: DictionaryPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentDictionaryBinding::bind)

    private lateinit var adapter: DictionaryAdapter

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
        initToolbar()
        setListeners()
        initAdapters()
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setListeners() {
        viewBinding.apply {
            adapter = DictionaryAdapter { position ->
                presenter.onItemClick(position)
            }
            pvLoad.onRetryClick = { onRetry() }
        }
    }

    private fun initAdapters() {
        viewBinding.rvDictionary.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DictionaryFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun showDictionaryList(dictionary: List<Dictionary>) {
        adapter.dictionaryList = dictionary
    }

    override fun showLoadingDialog(position: Int) {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.app_name)
            setMessage(R.string.dictionary_load_words)
            setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                presenter.loadDictionary(position)
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    override fun sendSuccessResult() {
        val localIntent = Intent(Constants.LOAD_WORDS_EVENT)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntent)
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
        presenter.loadDictionaryList()
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
