@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.select_translate

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.databinding.FragmentSelectTranslateBinding
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
class SelectTranslateFragment : MvpAppCompatFragment(), SelectTranslateView {

    @Inject
    lateinit var presenterProvider: Provider<SelectTranslatePresenter>
    private val presenter: SelectTranslatePresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentSelectTranslateBinding::bind)

    private val animationListener = object : Animator.AnimatorListener {
        @Suppress("EmptyFunctionBlock")
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            presenter.onAnimationEnd()
        }

        @Suppress("EmptyFunctionBlock")
        override fun onAnimationCancel(animation: Animator?) {
        }

        @Suppress("EmptyFunctionBlock")
        override fun onAnimationStart(animation: Animator?) {
        }
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
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.setHomeButtonEnabled(true)
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

            btnExit.setOnClickListener {
                presenter.onBackPressed()
            }
        }
    }

    private fun onClickTranslate(button: Button) {
        presenter.onTranslateClick(button.text.toString())
    }

    @Suppress("MagicNumber")
    override fun showWord(word: Word?, translates: List<String>) {
        with(viewBinding) {
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
    }

    override fun showEndGame(successCounter: Int, errorCounter: Int) {
        val localIntent = Intent(Constants.LOAD_WORDS_EVENT)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntent)
        with(viewBinding) {
            llContentContainer.makeGone()
            rlEnGameContainer.makeVisible()
            tvGameLevelSuccess.text = successCounter.toString()
            tvGameLevelError.text = errorCounter.toString()
        }
    }

    override fun showErrorAnimation() {
        with(viewBinding) {
            tvWord.makeGone()
            llTranslateContainer.makeGone()
            lavError.makeVisible()
            lavError.playAnimation()
            lavError.speed = 2f
        }
    }

    override fun showSuccessAnimation() {
        with(viewBinding) {
            tvWord.makeGone()
            llTranslateContainer.makeGone()
            lavSuccess.makeVisible()
            lavSuccess.playAnimation()
            lavError.speed = 2f
        }
    }

    override fun startLoading() {
        with(viewBinding) {
            pvLoad.startLoading()
            llContentContainer.makeGone()
        }
    }

    override fun errorLoading(errorMessage: String?) {
        with(viewBinding) {
            pvLoad.errorLoading(errorMessage)
            pvLoad.onRetryClick = { onRetry() }
            llContentContainer.makeGone()
        }
    }

    override fun completeLoading() {
        with(viewBinding) {
            pvLoad.completeLoading()
            llContentContainer.makeVisible()
        }
    }

    override fun onRetry() {
        presenter.loadWords()
    }
}
