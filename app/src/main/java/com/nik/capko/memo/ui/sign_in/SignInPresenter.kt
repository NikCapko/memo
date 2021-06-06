@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.sign_in

import com.nik.capko.memo.app.appStorage
import com.nik.capko.memo.utils.Constants
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SignInPresenter @Inject constructor() : MvpPresenter<SignInView>() {

    fun signIn(login: String, password: String) {
        appStorage.put(Constants.LOGIN, login)
        appStorage.put(Constants.PASSWORD, password)
        appStorage.put(Constants.IS_REGISTER, true)
        viewState.openWordsList()
    }

    fun skip() {
        appStorage.put(Constants.IS_REGISTER, false)
        appStorage.put(Constants.IS_SKIP_REGISTER, true)
        viewState.openWordsList()
    }
}
