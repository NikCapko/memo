package com.nik.capko.memo.data

data class WordType(
    var gender: String? = null,
    var type: String? = null,
    var formKey: String? = null,
) {
    companion object {
        const val WORD_TYPE_ADJECTIVE = "adj"
        const val WORD_TYPE_NOUN = "noun"
        const val WORD_TYPE_VERB = "verb"

        const val FORM_KEY = "#form"

        const val FIELD_GENDER = "gender"
        const val GENDER_M = "m"
        const val GENDER_F = "f"
        const val GENDER_N = "n"
    }
}
