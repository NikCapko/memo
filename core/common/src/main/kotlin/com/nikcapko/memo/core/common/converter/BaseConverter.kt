package com.nikcapko.memo.core.common.converter

fun interface BaseConverter<From, To> {
    fun convert(from: From): To
}
