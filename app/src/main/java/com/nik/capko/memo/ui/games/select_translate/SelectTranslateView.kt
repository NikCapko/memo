@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.select_translate

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SelectTranslateView : MvpView
