package com.nikcapko.memo.base.ui.command

interface CommandReceiver {
    fun <R, T : Command<R>> processCommand(command: T) {
        command.execute(this as R)
    }
}
