package com.nik.capko.memo.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nik.capko.memo.R
import com.nik.capko.memo.ui.words.list.WordListFragment
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity

@AndroidEntryPoint
class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openDefaultFragment()
    }

    private fun openDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcView, WordListFragment())
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
}
