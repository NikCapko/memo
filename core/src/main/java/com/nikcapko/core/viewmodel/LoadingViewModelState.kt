package com.nikcapko.core.viewmodel

sealed class LoadingViewModelState {
    object LoadingState : LoadingViewModelState()
    object NoItemsState : LoadingViewModelState()
    data class LoadedState<T>(var data: List<T>?) : LoadingViewModelState()
    data class ErrorState(var exception: Throwable) : LoadingViewModelState()
}
