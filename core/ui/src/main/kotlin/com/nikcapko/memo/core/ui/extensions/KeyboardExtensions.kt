@file:Suppress("TooManyFunctions")

package com.nikcapko.memo.core.ui.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

val Context.inputMethodManager: InputMethodManager?
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

/*
  ---------- Activity ----------
*/

fun Activity.hideKeyboard() {
    val view: View = currentFocus ?: window.decorView
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

/*
  ---------- Context ----------
*/

fun Context.showKeyboard(view: View) {
    view.isFocusable = true
    view.isFocusableInTouchMode = true
    view.requestFocus()
    inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun Context.hideKeyboard(view: View) {
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.hideKeyboard(activity: Activity) {
    val view: View = activity.currentFocus ?: activity.window.decorView
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.isShowKeyboard(): Boolean {
    return inputMethodManager?.isActive ?: false
}

/*
  ---------- Fragment ----------
*/

fun Fragment.showKeyboard(view: View) {
    activity?.showKeyboard(view)
}

fun Fragment.hideKeyboard(view: View) {
    activity?.hideKeyboard(view)
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Fragment.isShowKeyboard() {
    activity?.isShowKeyboard()
}

/*
  ---------- View ----------
*/

fun View.showKeyboard(view: View) {
    context.showKeyboard(view)
}

fun View.hideKeyboard(view: View) {
    context.hideKeyboard(view)
}

fun View.isShowKeyboard() {
    context.isShowKeyboard()
}
