package com.nik.capko.memo.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Form(
    var key: String? = null,
    var name: String? = null,
    var value: String? = null,
) : Parcelable
