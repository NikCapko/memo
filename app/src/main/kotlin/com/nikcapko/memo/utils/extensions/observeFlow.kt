package com.nikcapko.memo.utils.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

fun <T> Fragment.observeFlow(inputFlow: Flow<T>, callback: (T) -> Unit = {}) {
    viewLifecycleOwner.lifecycleScope.launch {
        inputFlow
            .flowWithLifecycle(lifecycle = viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .collect { callback(it) }
    }
}