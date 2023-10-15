@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.select_translate

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.R
import com.nikcapko.memo.base.ui.BaseFragment
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.databinding.FragmentSelectTranslateBinding
import com.nikcapko.memo.utils.Constants
import com.nikcapko.memo.utils.extensions.makeGone
import com.nikcapko.memo.utils.extensions.makeVisible
import com.nikcapko.memo.utils.extensions.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@Suppress("TooManyFunctions")
@AndroidEntryPoint
internal class SelectTranslateFragment : BaseFragment() {

    private val viewModel by viewModels<SelectTranslateViewModel>()
    private val viewBinding by viewBinding(FragmentSelectTranslateBinding::bind)
    private val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) = Unit
        override fun onAnimationCancel(animation: Animator) = Unit
        override fun onAnimationRepeat(animation: Animator) = Unit
        override fun onAnimationEnd(animation: Animator) = viewModel.onAnimationEnd()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setListeners()
        observe()
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setListeners() {
        with(viewBinding) {
            lavError.addAnimatorListener(animationListener)
            lavSuccess.addAnimatorListener(animationListener)

            btnTranslate1.setOnClickListener { onClickTranslate(it as Button) }
            btnTranslate2.setOnClickListener { onClickTranslate(it as Button) }
            btnTranslate3.setOnClickListener { onClickTranslate(it as Button) }
            btnTranslate4.setOnClickListener { onClickTranslate(it as Button) }
            btnTranslate5.setOnClickListener { onClickTranslate(it as Button) }

            btnExit.setOnClickListener { viewModel.onBackPressed() }
            pvLoad.onRetryClick = ::onRetry
        }
    }

    private fun observe() {
        observeFlow(viewModel.state) { state ->
            when (state) {
                DataLoadingViewModelState.LoadingState -> startLoading()
                DataLoadingViewModelState.NoItemsState -> showWord(null, emptyList())
                is DataLoadingViewModelState.LoadedState<*> -> {
                    val data = state.data as? Pair<Word, List<String>>
                    showWord(data?.first, data?.second.orEmpty())
                    completeLoading()
                }

                is DataLoadingViewModelState.ErrorState -> {
                    errorLoading(requireContext().getString(R.string.error_default_message))
                }
            }
        }
        viewModel.successAnimationEvent.observe(viewLifecycleOwner) {
            it.data?.let { successAnimation ->
                if (successAnimation) {
                    showSuccessAnimation()
                } else {
                    showErrorAnimation()
                }
            }
        }
        viewModel.endGameEvent.observe(viewLifecycleOwner) { endGameResult ->
            endGameResult.data?.let { showEndGame(it.first, it.second) }
        }
    }

    private fun onClickTranslate(button: Button) {
        viewModel.onTranslateClick(button.text.toString())
    }

    @Suppress("MagicNumber")
    private fun showWord(word: Word?, translates: List<String>) = with(viewBinding) {
        lavSuccess.makeGone()
        lavError.makeGone()
        tvWord.makeVisible()
        llTranslateContainer.makeVisible()
        tvWord.text = word?.word
        btnTranslate1.text = translates[0]
        btnTranslate2.text = translates[1]
        btnTranslate3.text = translates[2]
        btnTranslate4.text = translates[3]
        btnTranslate5.text = translates[4]
    }

    private fun showEndGame(successCounter: Int, errorCounter: Int) = with(viewBinding) {
        val localIntent = Intent(Constants.LOAD_WORDS_EVENT)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntent)
        llContentContainer.makeGone()
        rlEnGameContainer.makeVisible()
        tvGameLevelSuccess.text = successCounter.toString()
        tvGameLevelError.text = errorCounter.toString()
    }

    private fun showErrorAnimation() = with(viewBinding) {
        tvWord.makeGone()
        llTranslateContainer.makeGone()
        lavError.makeVisible()
        lavError.playAnimation()
        lavError.speed = 2f
    }

    private fun showSuccessAnimation() = with(viewBinding) {
        tvWord.makeGone()
        llTranslateContainer.makeGone()
        lavSuccess.makeVisible()
        lavSuccess.playAnimation()
        lavError.speed = 2f
    }

    private fun startLoading() = with(viewBinding) {
        pvLoad.startLoading()
        llContentContainer.makeGone()
    }

    private fun errorLoading(errorMessage: String?) = with(viewBinding) {
        pvLoad.errorLoading(errorMessage)
        llContentContainer.makeGone()
    }

    private fun completeLoading() = with(viewBinding) {
        pvLoad.completeLoading()
        llContentContainer.makeVisible()
    }

    private fun onRetry() {
        viewModel.loadWords()
    }
}
