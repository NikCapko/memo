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

        const val PHRASE_TYPE_ADJ_NONE_F = "adj#form[f_gender] noun#gender=f"
        const val PHRASE_TYPE_ADJ_NONE_M = "adj#form[m_gender] noun#gender=m"
        const val PHRASE_TYPE_ADJ_NONE_N = "adj#form[n_gender] noun#gender=n"
    }
}
