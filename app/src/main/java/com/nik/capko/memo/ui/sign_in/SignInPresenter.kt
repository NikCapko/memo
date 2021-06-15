@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.sign_in

import com.github.terrakok.cicerone.Router
import com.nik.capko.memo.app.appStorage
import com.nik.capko.memo.ui.Screens
import com.nik.capko.memo.utils.Constants
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SignInPresenter @Inject constructor(
    private val router: Router,
) : MvpPresenter<SignInView>() {

    fun signIn(login: String, password: String) {
        appStorage.put(Constants.LOGIN, login)
        appStorage.put(Constants.PASSWORD, password)
        appStorage.put(Constants.IS_REGISTER, true)
        router.navigateTo(Screens.wordListScreen())
    }

    fun skip() {
        appStorage.put(Constants.IS_REGISTER, false)
        appStorage.put(Constants.IS_SKIP_REGISTER, true)
        router.navigateTo(Screens.wordListScreen())
    }
}
