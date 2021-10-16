@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.sign_in

import com.github.terrakok.cicerone.Router
import com.nikcapko.memo.ui.Screens
import com.nikcapko.memo.utils.AppStorage
import com.nikcapko.memo.utils.Constants
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SignInPresenter @Inject constructor(
    private val router: Router,
    private val appStorage: AppStorage,
) : MvpPresenter<SignInView>() {

    fun signIn(login: String, password: String) {
        appStorage.put(Constants.LOGIN, login)
        appStorage.put(Constants.PASSWORD, password)
        appStorage.put(Constants.IS_REGISTER, true)
        router.replaceScreen(Screens.wordListScreen())
    }

    fun skip() {
        appStorage.put(Constants.IS_REGISTER, false)
        appStorage.put(Constants.IS_SKIP_REGISTER, true)
        router.replaceScreen(Screens.wordListScreen())
    }
}
