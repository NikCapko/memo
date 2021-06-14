package com.nik.capko.memo.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.nik.capko.memo.R
import com.nik.capko.memo.app.appStorage
import com.nik.capko.memo.ui.sign_in.SignInFragment
import com.nik.capko.memo.ui.words.list.WordListFragment
import com.nik.capko.memo.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity

@AndroidEntryPoint
class MainActivity : MvpAppCompatActivity() {

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
            openFragment(WordListFragment::class.java)
        } else {
            openFragment(SignInFragment::class.java)
        }
    }

    fun <T : Fragment> replaceFragment(clazz: Class<T>, bundle: Bundle? = null) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcView, newInstance(clazz, bundle))
            .addToBackStack(clazz.name)
            .commitAllowingStateLoss()
    }

    fun <T : Fragment> openFragment(clazz: Class<T>, bundle: Bundle? = null) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fcView, newInstance(clazz, bundle))
            .addToBackStack(clazz.name)
            .commitAllowingStateLoss()
    }

    private fun <T : Fragment> newInstance(clazz: Class<T>, bundle: Bundle?): T {
        return clazz.newInstance().apply {
            if (bundle != null) {
                arguments = bundle
            }
        }
    }

    fun closeFragment() {
        supportFragmentManager.popBackStack()
    }
}
