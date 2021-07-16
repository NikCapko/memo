@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.games.find_pairs

import com.nikcapko.memo.base.view.ProgressMvpView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FindPairsView : MvpView, ProgressMvpView {
    fun showWords(wordsList: List<String?>, translateList: List<String?>)
    fun onFindPairResult(success: Boolean)
    fun endGame()
}
