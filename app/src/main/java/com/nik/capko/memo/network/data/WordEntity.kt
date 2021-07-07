package com.nik.capko.memo.network.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordEntity(
    @SerializedName("id") var id: Long,
    @SerializedName("word") var word: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("translation") var translation: String? = null,
    @SerializedName("frequency") var frequency: Float? = null,
    @SerializedName("primaryLanguage") var primaryLanguage: Boolean = false,

    @SerializedName("forms") var forms: List<FormEntity>? = null
) : Parcelable
