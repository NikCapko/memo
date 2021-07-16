@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.sign_in

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SignInView : MvpView
