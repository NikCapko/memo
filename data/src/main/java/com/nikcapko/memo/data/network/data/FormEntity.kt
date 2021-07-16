package com.nikcapko.memo.data.network.data

import com.google.gson.annotations.SerializedName

data class FormEntity(
    @SerializedName("key") var key: String,
    @SerializedName("name") var name: String? = null,
    @SerializedName("value") var value: String? = null,
)
