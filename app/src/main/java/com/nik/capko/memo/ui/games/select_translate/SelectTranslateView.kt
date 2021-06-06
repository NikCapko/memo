@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.select_translate

import com.nik.capko.memo.base.view.ProgressMvpView
import com.nik.capko.memo.data.Word
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SelectTranslateView : MvpView, ProgressMvpView {
    fun showWord(word: Word?, translates: List<String>)
    fun showErrorAnimation()
    fun showSuccessAnimation()
    fun showEndGame(successCounter: Int, errorCounter: Int)
}
