package com.nikcapko.memo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dictionary(
    var id: String,
    var name: String,
    var size: Long = 0,
) : Parcelable
