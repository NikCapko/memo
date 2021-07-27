package com.nikcapko.core.viewmodel

sealed class DataLoadingViewModelState {
    object LoadingState : DataLoadingViewModelState()
    object NoItemsState : DataLoadingViewModelState()
    data class LoadedState<T>(var data: T?) : DataLoadingViewModelState()
    data class ErrorState(var exception: Throwable) : DataLoadingViewModelState()
}
