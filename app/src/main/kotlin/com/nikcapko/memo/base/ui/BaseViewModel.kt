package com.nikcapko.memo.base.ui

import androidx.lifecycle.ViewModel

internal abstract class BaseViewModel : ViewModel() {
    open fun setArguments(vararg params: Any?) = Unit
}