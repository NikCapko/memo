package com.nikcapko.memo.utils.extensions

import android.view.View

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE
