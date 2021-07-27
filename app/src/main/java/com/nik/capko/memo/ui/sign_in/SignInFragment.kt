@file:Suppress("PackageName", "PackageNaming")

package com.nik.capko.memo.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nik.capko.memo.R
import com.nik.capko.memo.databinding.FragmentSignInBinding
import com.nik.capko.memo.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SignInFragment : MvpAppCompatFragment(), SignInView {

    @Inject
    lateinit var presenterProvider: Provider<SignInPresenter>
    private val presenter: SignInPresenter by moxyPresenter { presenterProvider.get() }

    private val viewBinding by viewBinding(FragmentSignInBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setListeners()
    }

    private fun initToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? AppCompatActivity)?.supportActionBar?.setHomeButtonEnabled(false)
    }

    private fun setListeners() {
        viewBinding.btnSingIn.setOnClickListener {
            hideKeyboard()
            presenter.signIn(
                viewBinding.etLogin.text.toString(),
                viewBinding.etPassword.text.toString(),
            )
        }
        viewBinding.btnSkip.setOnClickListener {
            presenter.skip()
        }
    }
}
