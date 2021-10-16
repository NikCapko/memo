package com.nikcapko.memo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Form(
    var key: String,
    var name: String,
    var value: String,
) : Parcelable
