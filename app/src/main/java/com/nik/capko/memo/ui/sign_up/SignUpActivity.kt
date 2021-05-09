package com.nik.capko.memo.ui.sign_up

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SignUpActivity : MvpAppCompatActivity() {

    @Inject
    lateinit var presenterProvider: Provider<SignUpPresenter>
    private val presenter: SignUpPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(ActivitySignUpBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setListeners()
    }

    private fun setListeners() {
    }
}
