package com.nikcapko.memo.core.common.converter

fun <From, To> Iterable<From>.convertEach(converter: BaseConverter<From, To>): List<To> {
    return map(converter::convert)
}

fun <From, To> From.convert(converter: BaseConverter<From, To>): To {
    return converter.convert(this)
}
