package com.nikcapko.memo.base.ui

import kotlinx.coroutines.flow.Flow

interface StateFlowWrapper<T> {
    fun value(): T
    fun liveValue(): Flow<T>
    fun update(value: T)

    fun update(func: (T) -> T) {
        apply { update(func(value())) }
    }
}
