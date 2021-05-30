@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.games.find_pairs

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FindPairsView : MvpView
