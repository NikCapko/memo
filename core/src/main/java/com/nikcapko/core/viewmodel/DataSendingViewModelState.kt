package com.nikcapko.core.viewmodel

sealed class DataSendingViewModelState {
    object SendingState : DataSendingViewModelState()
    object SentState : DataSendingViewModelState()
    data class ErrorState(var exception: Throwable) : DataSendingViewModelState()
}
