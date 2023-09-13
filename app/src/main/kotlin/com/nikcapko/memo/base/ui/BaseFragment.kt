package com.nikcapko.memo.base.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.nikcapko.memo.ui.main.ChainHolder
import java.lang.ref.WeakReference

open class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity = activity
        (activity as? ChainHolder)?.chain?.add(WeakReference<Fragment>(this))
    }

    override fun onDetach() {
        val activity = activity
        if (activity is ChainHolder) {
            val chain = (activity as ChainHolder).chain
            val it = chain.iterator()
            while (it.hasNext()) {
                val fragmentReference = it.next()
                val fragment = fragmentReference.get()
                if (fragment != null && fragment === this) {
                    it.remove()
                    break
                }
            }
        }
        super.onDetach()
    }
}
