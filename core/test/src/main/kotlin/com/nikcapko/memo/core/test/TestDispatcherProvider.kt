package com.nikcapko.memo.core.test

import com.nikcapko.memo.core.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
class TestDispatcherProvider(
    coroutineDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
) : DispatcherProvider {
    override val main: CoroutineDispatcher = coroutineDispatcher
    override val default: CoroutineDispatcher = coroutineDispatcher
    override val io: CoroutineDispatcher = coroutineDispatcher
    override val unconfined: CoroutineDispatcher = coroutineDispatcher
}
