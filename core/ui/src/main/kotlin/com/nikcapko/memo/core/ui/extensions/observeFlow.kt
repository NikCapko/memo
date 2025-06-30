package com.nikcapko.memo.core.ui.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> Fragment.observe(
    inputFlow: Flow<T>,
    crossinline callback: (T) -> Unit = {},
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
) {
    viewLifecycleOwner.lifecycleScope.launch {
        inputFlow
            .flowWithLifecycle(lifecycle = viewLifecycleOwner.lifecycle, minActiveState = minActiveState)
            .collect { callback(it) }
    }
}
