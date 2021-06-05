package com.nik.capko.memo.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Word(
    @SerializedName("id") var id: Long,
    @SerializedName("word") var word: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("translation") var translation: String? = null,
    @SerializedName("frequency") var frequency: Float? = null,
    @SerializedName("primaryLanguage") var primaryLanguage: Boolean = false,

    @SerializedName("forms") var forms: List<Form>? = null
) : Parcelable {
    companion object {
        const val WORD_GAME_PRICE = 0.02f
    }
}
