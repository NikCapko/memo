package com.nik.capko.memo.utils.view

interface IProgressView {
    fun startLoading()
    fun errorLoading(errorMessage: String?)
    fun completeLoading()
}
