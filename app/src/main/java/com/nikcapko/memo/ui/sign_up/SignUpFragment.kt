@file:Suppress("PackageName", "PackageNaming")

package com.nikcapko.memo.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nikcapko.memo.R
import com.nikcapko.memo.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SignUpFragment : MvpAppCompatFragment(), SignUpView {

    @Inject
    lateinit var presenterProvider: Provider<SignUpPresenter>
    private val presenter: SignUpPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentSignUpBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    @Suppress("EmptyFunctionBlock")
    private fun setListeners() {
    }
}
