@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.sign_up

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SignUpView : MvpView
