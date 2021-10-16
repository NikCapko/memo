package com.nikcapko.memo.data.network.data

import com.google.gson.annotations.SerializedName

data class DictionaryEntity(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("size") var size: Long = 0,
)
