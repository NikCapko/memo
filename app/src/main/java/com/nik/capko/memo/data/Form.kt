package com.nik.capko.memo.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Form(
    @SerializedName("key") var key: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("value") var value: String? = null,
) : Parcelable
