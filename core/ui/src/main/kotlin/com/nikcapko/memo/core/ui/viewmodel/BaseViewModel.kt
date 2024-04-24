package com.nikcapko.memo.core.ui.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    open fun setArguments(vararg params: Any?) = Unit
}