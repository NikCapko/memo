@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.find_pairs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.R
import com.nikcapko.memo.base.ui.BaseFragment
import com.nikcapko.memo.databinding.FragmentFindPairsBinding
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeInvisible
import com.nikcapko.memo.utils.extensions.makeVisible
import com.nikcapko.memo.utils.extensions.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class FindPairsFragment : BaseFragment() {

    private val viewModel by viewModels<FindPairsViewModel>()
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
        initToolbar()
        setListeners()
        observe()
    }

    private fun observe() {
        observeFlow(viewModel.state) { state ->
            when (state) {
                DataLoadingViewModelState.LoadingState -> startLoading()
                DataLoadingViewModelState.NoItemsState -> showWords(emptyList(), emptyList())
                is DataLoadingViewModelState.LoadedState<*> -> {
                    val data = state.data as? Pair<List<String>, List<String>>
                    showWords(data?.first.orEmpty(), data?.second.orEmpty())
                    completeLoading()
                }

                is DataLoadingViewModelState.ErrorState -> {
                    errorLoading(requireContext().getString(R.string.error_default_message))
                }
            }
        }
        viewModel.findPairResultEvent.observe(viewLifecycleOwner) {
            it.data?.let { onFindPairResult(it) }
        }
        viewModel.endGameEvent.observe(viewLifecycleOwner) {
            it?.let { endGame() }
        }
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setListeners() = with(viewBinding) {
        rgWord.setOnCheckedChangeListener { group, checkedId ->
            if (rgTranslate.checkedRadioButtonId != -1) {
                selectedTranslate = rgTranslate.findViewById(rgTranslate.checkedRadioButtonId)
                selectedWord = group.findViewById(checkedId)
                viewModel.onFindPair(
                    selectedWord = selectedWord?.text.toString(),
                    selectedTranslate = selectedTranslate?.text.toString()
                )
                rgTranslate.clearCheck()
                rgWord.clearCheck()
            }
        }
        rgTranslate.setOnCheckedChangeListener { group, checkedId ->
            if (rgWord.checkedRadioButtonId != -1) {
                selectedWord = rgWord.findViewById(rgWord.checkedRadioButtonId)
                selectedTranslate = group.findViewById(checkedId)
                viewModel.onFindPair(
                    selectedWord = selectedWord?.text.toString(),
                    selectedTranslate = selectedTranslate?.text.toString()
                )
                rgWord.clearCheck()
                rgTranslate.clearCheck()
            }
        }
        btnExit.setOnClickListener {
            viewModel.onBackPressed()
        }
        lavSuccess.setOnClickListener { }
        pvLoad.onRetryClick = ::onRetry
    }

    @Suppress("MagicNumber")
    private fun showWords(
        wordsList: List<String?>,
        translateList: List<String?>,
    ) = with(viewBinding) {
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

    private fun onFindPairResult(success: Boolean) {
        if (success) {
            selectedWord?.makeInvisible()
            selectedTranslate?.makeInvisible()
        }
        selectedWord = null
        selectedTranslate = null
    }

    private fun endGame() = with(viewBinding) {
        flContentContainer.makeGone()
        rlEnGameContainer.makeVisible()
        lavSuccess.playAnimation()
    }

    private fun startLoading() = with(viewBinding) {
        pvLoad.startLoading()
        flContentContainer.makeGone()
    }

    private fun errorLoading(errorMessage: String?) = with(viewBinding) {
        pvLoad.errorLoading(errorMessage)
        flContentContainer.makeGone()
    }

    private fun completeLoading() = with(viewBinding) {
        pvLoad.completeLoading()
        flContentContainer.makeVisible()
    }

    private fun onRetry() {
        viewModel.loadWords()
    }
}
