package com.nik.capko.memo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Word(
    var id: Long,
    var word: String? = null,
    var type: String? = null,
    var translation: String? = null,
    var frequency: Float? = null,

    var forms: List<Form>? = null
) : Parcelable
