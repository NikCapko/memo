@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.sign_in

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SignInView : MvpView
