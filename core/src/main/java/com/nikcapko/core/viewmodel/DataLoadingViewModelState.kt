package com.nikcapko.core.viewmodel

sealed class DataLoadingViewModelState {
    data object LoadingState : DataLoadingViewModelState()
    data object NoItemsState : DataLoadingViewModelState()
    data class LoadedState<T>(var data: T?) : DataLoadingViewModelState()
    data class ErrorState(var exception: Throwable) : DataLoadingViewModelState()
}
