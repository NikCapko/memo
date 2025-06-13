package com.nikcapko.memo.core.common.converter

interface BaseListConverter<From, To> : BaseConverter<List<From>, List<To>> {

    val itemConverter: BaseConverter<From, To>

    override fun convert(from: List<From>): List<To> {
        return from.convertEach(itemConverter)
    }
}
