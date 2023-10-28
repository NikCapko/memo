package com.nikcapko.memo

import com.nikcapko.memo.base.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
internal class TestDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val default: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher()
    override val unconfined: CoroutineDispatcher = UnconfinedTestDispatcher()
}
