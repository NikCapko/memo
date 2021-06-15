package com.nik.capko.memo.network.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormEntity(
    @SerializedName("key") var key: String,
    @SerializedName("name") var name: String? = null,
    @SerializedName("value") var value: String? = null,
) : Parcelable
