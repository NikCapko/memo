package com.nik.capko.memo.ui.games.phrases

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.FragmentPhrasesBinding
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class PhrasesFragment : MvpAppCompatFragment(), PhrasesView {

    @Suppress("ClassOrdering")
    companion object {
        const val WORDS = "SelectTranslateFragment.WORDS"
    }

    @Inject
    lateinit var presenterProvider: Provider<PhrasesPresenter>
    private val presenter: PhrasesPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentPhrasesBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phrases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(viewBinding) {
            tvTranslate.setOnClickListener {
                tvTranslate.text = ""
            }
        }
    }

    override fun initView(phrase: String, translates: MutableList<String?>?) {
        with(viewBinding) {
            tvPhrase.text = phrase
            val ids = mutableListOf<Int>()
            translates?.forEach { it ->
                val button = MaterialButton(requireContext())
                button.id = View.generateViewId()
                button.text = it
                button.setOnClickListener { it1 ->
                    onAddTranslate((it1 as Button).text.toString())
                }
                ids.add(button.id)
                clContainer.addView(button)
            }
            fTranslates.referencedIds = ids.toIntArray()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onAddTranslate(text: String) {
        with(viewBinding) {
            tvTranslate.text = tvTranslate.text.toString() + " " + text
            presenter.checkPhraseTranslate(tvTranslate.text.toString())
        }
    }

    override fun showMessage(text: String) {
        Toast.makeText(context, "$text", Toast.LENGTH_SHORT).show()
    }

    override fun startLoading() {
        with(viewBinding) {
            pvLoad.makeVisible()
            tvPhrase.makeGone()
            tvTranslate.makeGone()
            fTranslates.makeGone()
        }
    }

    override fun errorLoading(errorMessage: String?) {
        with(viewBinding) {
            pvLoad.makeGone()
            tvPhrase.makeGone()
            tvTranslate.makeGone()
            fTranslates.makeGone()
        }
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.makeGone()
            tvPhrase.makeVisible()
            tvTranslate.makeVisible()
            fTranslates.makeVisible()
        }
    }
}
