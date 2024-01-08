package com.nikcapko.memo.utils.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Fragment.observe(inputFlow: Flow<T>, callback: (T) -> Unit = {}) {
    viewLifecycleOwner.lifecycleScope.launch {
        inputFlow
            .flowWithLifecycle(lifecycle = viewLifecycleOwner.lifecycle)
            .collect { callback(it) }
    }
}

fun <T> Fragment.observe(input: LiveData<T>, callback: (T) -> Unit = {}) {
    input.observe(viewLifecycleOwner) {
        callback(it)
    }
}