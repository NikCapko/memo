package com.nik.capko.memo.ui.games.phrases

import com.nik.capko.memo.data.Word
import com.nik.capko.memo.data.WordType
import com.nik.capko.memo.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject

class PhrasesPresenter @Inject constructor(
    private val gameRepository: GameRepository
) : MvpPresenter<PhrasesView>() {

    private var translatePhrase: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initView()
    }

    private fun initView() {
        loadWords()
    }

    fun loadWords() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }

            val wordForGame = gameRepository.getWordsForGame()
            val allWords = gameRepository.getWords()

            val adjWord = wordForGame.firstOrNull { it.type == WordType.WORD_TYPE_ADJECTIVE }
            val noneWord = wordForGame.firstOrNull { it.type == WordType.WORD_TYPE_NOUN }
            val verbWord = wordForGame.firstOrNull { it.type == WordType.WORD_TYPE_VERB }
            val adjWordTranslate = allWords.firstOrNull { it.word == adjWord?.translation }
            val noneWordTranslate = allWords.firstOrNull { it.word == noneWord?.translation }
            val verbWordTranslate = allWords.firstOrNull { it.word == verbWord?.translation }

            checkTranslate(adjWordTranslate, noneWordTranslate, verbWordTranslate)

            val phrase = adjWord?.word + " " + noneWord?.word + " " + verbWord?.word
            val traslates = adjWordTranslate?.forms
                ?.map { it.value }
                ?.toMutableList()
            traslates?.add(noneWordTranslate?.word)
            traslates?.addAll(verbWordTranslate?.forms?.map { it.value }.orEmpty())
            traslates?.shuffle()

            launch(Dispatchers.Main) {
                viewState.initView(phrase, traslates)
                viewState.completeLoading()
            }
        }
    }

    @Suppress("NestedBlockDepth", "CollapsibleIfStatements")
    private fun CoroutineScope.checkTranslate(
        adjWordTranslate: Word?,
        noneWordTranslate: Word?,
        verbWordTranslate: Word?
    ) =
        launch {
            val phrasePattern = when (noneWordTranslate?.gender) {
                WordType.GENDER_F -> WordType.PHRASE_TYPE_ADJ_NONE_VERB_F
                WordType.GENDER_M -> WordType.PHRASE_TYPE_ADJ_NONE_VERB_M
                WordType.GENDER_N -> WordType.PHRASE_TYPE_ADJ_NONE_VERB_N
                else -> ""
            }
            val wordTypes: List<WordType> = parseStringPattern(phrasePattern)
            var text = ""
            val words = listOf(adjWordTranslate, noneWordTranslate, verbWordTranslate)
            wordTypes.forEach { wordType ->
                words.forEach { word ->
                    if (wordType.type == word?.type) {
                        if ((word?.gender == null || wordType.gender == null) || wordType.gender == word.gender) {
                            if (!word?.forms.isNullOrEmpty() && !wordType.formKey.isNullOrEmpty()) {
                                val form = word?.forms?.firstOrNull { it.key == wordType.formKey }
                                if (form != null) {
                                    text += " " + form.value
                                }
                            } else {
                                text += " " + word?.word
                            }
                        }
                    }
                }
            }
            translatePhrase = text
        }

    private fun parseStringPattern(stringPatter: String): List<WordType> {
        val wordTypes = mutableListOf<WordType>()
        val fields = stringPatter.split(" ")
        fields.forEach { word ->
            val wordType = WordType()
            when {
                word.startsWith(WordType.WORD_TYPE_NOUN) -> wordType.type = WordType.WORD_TYPE_NOUN
                word.startsWith(WordType.WORD_TYPE_ADJECTIVE) ->
                    wordType.type =
                        WordType.WORD_TYPE_ADJECTIVE
                word.startsWith(WordType.WORD_TYPE_VERB) -> wordType.type = WordType.WORD_TYPE_VERB
            }
            if (word.contains(WordType.FORM_KEY)) {
                val key =
                    word.substring(word.indexOf(WordType.FORM_KEY) + WordType.FORM_KEY.length)
                        .removePrefix("[")
                        .removeSuffix("]")
                wordType.formKey = key
            } else if (word.contains("#")) {
                val field = word.substring(word.indexOf("#") + 1).split("=").getOrNull(0)
                val key = word.substring(word.indexOf("#") + 1).split("=").getOrNull(1)
                when (field) {
                    WordType.FIELD_GENDER -> wordType.gender = key
                }
            }
            wordTypes.add(wordType)
        }
        return wordTypes
    }

    fun checkPhraseTranslate(translate: String) {
        if (translate == translatePhrase) {
            viewState.showMessage("Правильно")
        }
    }
}
