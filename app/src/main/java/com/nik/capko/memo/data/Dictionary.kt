package com.nik.capko.memo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dictionary(
    var id: String? = null,
    var name: String? = null,
    var size: Long = 0,
) : Parcelable
