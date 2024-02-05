package com.nikcapko.memo.base.ui.viewmodel

import androidx.lifecycle.ViewModel

internal abstract class BaseViewModel : ViewModel() {
    open fun setArguments(vararg params: Any?) = Unit
}