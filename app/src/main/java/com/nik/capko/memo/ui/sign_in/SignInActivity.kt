package com.nik.capko.memo.ui.sign_in

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.ActivitySignInBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SignInActivity : MvpAppCompatActivity() {

    @Inject
    lateinit var presenterProvider: Provider<SignInPresenter>
    private val presenter: SignInPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(ActivitySignInBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setListeners()
    }

    private fun setListeners() {
    }
}
