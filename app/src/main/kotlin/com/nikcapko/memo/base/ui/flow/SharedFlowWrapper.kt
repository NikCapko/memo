package com.nikcapko.memo.base.ui.flow

import kotlinx.coroutines.flow.Flow

interface SharedFlowWrapper<T> {
    fun liveValue(): Flow<T>
    suspend fun update(value: T)
}
