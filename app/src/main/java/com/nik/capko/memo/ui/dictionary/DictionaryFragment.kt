package com.nik.capko.memo.ui.dictionary

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.databinding.FragmentDictionaryBinding
import com.nik.capko.memo.ui.dictionary.adapter.DictionaryAdapter
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class DictionaryFragment : MvpAppCompatFragment(), DictionaryView {

    @Inject
    lateinit var presenterProvider: Provider<DictionaryPresenter>
    private val presenter: DictionaryPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentDictionaryBinding::bind)

    private lateinit var adapter: DictionaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initAdapters()
    }

    private fun setListeners() {
        adapter = DictionaryAdapter { position ->
            presenter.onItemClick(position)
        }
    }

    private fun initAdapters() {
        viewBinding.rvDictionary.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DictionaryFragment.adapter
        }
    }

    override fun showDictionaryList(dictionary: List<Dictionary>) {
        TODO("Not yet implemented")
    }

    override fun showLoadingDialog(position: Int) {
        AlertDialog.Builder(activity).apply {
            setTitle(R.string.app_name)
            setMessage(R.string.app_name)
            setCancelable(true)
            setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
                presenter.loadDictionary(position)
            }
        }.create().show()
    }

    override fun startLoading() {
        viewBinding.pvLoad.makeVisible()
        viewBinding.rvDictionary.makeVisible()
    }

    override fun errorLoading(errorMessage: String?) {
        Toast.makeText(activity, "$errorMessage", Toast.LENGTH_SHORT).show()
        viewBinding.pvLoad.makeGone()
        viewBinding.rvDictionary.makeGone()
    }

    override fun completeLoading() {
        viewBinding.pvLoad.makeGone()
        viewBinding.rvDictionary.makeVisible()
    }
}
