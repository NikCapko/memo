package com.nikcapko.memo.core.ui.command

interface Command<R : CommandReceiver> {
    fun execute(receiver: R)
}
