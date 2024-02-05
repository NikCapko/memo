package com.nikcapko.memo.base.ui.command

interface Command<R : CommandReceiver> {
    fun execute(receiver: R)
}
