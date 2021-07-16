package com.nikcapko.memo.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.nikcapko.memo.R
import com.nikcapko.memo.ui.Screens
import com.nikcapko.memo.utils.AppStorage
import com.nikcapko.memo.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : MvpAppCompatActivity(), ChainHolder {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var appStorage: AppStorage

    override val chain = ArrayList<WeakReference<Fragment>>()

    private val navigator: Navigator = object : AppNavigator(this, R.id.fcView) {

        override fun applyCommands(commands: Array<out Command>) {
            super.applyCommands(commands)
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        openDefaultFragment()
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDefaultFragment() {
        if (appStorage.get(Constants.IS_REGISTER, false) ||
            appStorage.get(Constants.IS_SKIP_REGISTER, false)
        ) {
            navigator.applyCommands(arrayOf<Command>(Replace(Screens.wordListScreen())))
        } else {
            navigator.applyCommands(arrayOf<Command>(Replace(Screens.signInScreen())))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
