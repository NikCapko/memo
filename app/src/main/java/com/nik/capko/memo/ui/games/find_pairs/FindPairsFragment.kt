@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.find_pairs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.FragmentFindPairsBinding
import com.nik.capko.memo.utils.extensions.makeGone
import com.nik.capko.memo.utils.extensions.makeInvisible
import com.nik.capko.memo.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class FindPairsFragment : MvpAppCompatFragment(), FindPairsView {

    @Inject
    lateinit var presenterProvider: Provider<FindPairsPresenter>
    private val presenter: FindPairsPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentFindPairsBinding::bind)

    private var selectedTranslate: RadioButton? = null
    private var selectedWord: RadioButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_pairs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(viewBinding) {
            rgWord.setOnCheckedChangeListener { group, checkedId ->
                if (rgTranslate.checkedRadioButtonId != -1) {
                    selectedTranslate =
                        rgTranslate.findViewById(rgTranslate.checkedRadioButtonId)
                    selectedWord = group.findViewById(checkedId)
                    presenter.onFindPair(
                        selectedWord?.text.toString(),
                        selectedTranslate?.text.toString()
                    )
                    rgTranslate.clearCheck()
                    rgWord.clearCheck()
                }
            }
            rgTranslate.setOnCheckedChangeListener { group, checkedId ->
                if (rgWord.checkedRadioButtonId != -1) {
                    selectedWord =
                        rgWord.findViewById(rgWord.checkedRadioButtonId)
                    selectedTranslate = group.findViewById(checkedId)
                    presenter.onFindPair(
                        selectedWord?.text.toString(),
                        selectedTranslate?.text.toString()
                    )
                    rgWord.clearCheck()
                    rgTranslate.clearCheck()
                }
            }
            btnExit.setOnClickListener {
                presenter.onBackPressed()
            }
            lavSuccess.setOnClickListener { }
        }
    }

    @Suppress("MagicNumber")
    override fun showWords(wordsList: List<String?>, translateList: List<String?>) {
        with(viewBinding) {
            btnWord1.text = wordsList[0]
            btnWord2.text = wordsList[1]
            btnWord3.text = wordsList[2]
            btnWord4.text = wordsList[3]
            btnWord5.text = wordsList[4]

            btnTranslate1.text = translateList[0]
            btnTranslate2.text = translateList[1]
            btnTranslate3.text = translateList[2]
            btnTranslate4.text = translateList[3]
            btnTranslate5.text = translateList[4]
        }
    }

    override fun onFindPairResult(success: Boolean) {
        if (success) {
            selectedWord?.makeInvisible()
            selectedTranslate?.makeInvisible()
        }
        selectedWord = null
        selectedTranslate = null
    }

    override fun endGame() {
        with(viewBinding) {
            flContentContainer.makeGone()
            rlEnGameContainer.makeVisible()
            lavSuccess.playAnimation()
        }
    }

    override fun startLoading() {
        with(viewBinding) {
            pvLoad.startLoading()
            flContentContainer.makeGone()
        }
    }

    override fun errorLoading(errorMessage: String?) {
        with(viewBinding) {
            pvLoad.errorLoading(errorMessage)
            pvLoad.onRetryClick = { onRetry() }
            flContentContainer.makeGone()
        }
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.completeLoading()
            flContentContainer.makeVisible()
        }
    }

    override fun onRetry() {
        presenter.loadWords()
    }
}
