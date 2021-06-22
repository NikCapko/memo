package com.nik.capko.memo.ui.words.detail

import com.github.terrakok.cicerone.Router
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.repository.WordRepository
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import java.util.Date
import javax.inject.Inject

class WordDetailPresenter @Inject constructor(
    private val router: Router,
    private val wordRepository: WordRepository,
) : MvpPresenter<WordDetailView>() {

    private var word: Word? = null

    private var disposable = Disposables.empty()

    fun setArguments(vararg params: Any?) {
        word = params[0] as? Word
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        initView()
    }

    private fun initView() {
        viewState.initView(word)
    }

    fun onSaveWord(wordArg: String, translate: String) {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startProgressDialog()
            }
            val wordDBEntity = if (word != null) {
                WordDBEntity(
                    word?.id ?: Date().time,
                    wordArg,
                    word?.type,
                    word?.gender,
                    translate,
                    word?.frequency,
                    true
                )
            } else {
                WordDBEntity(
                    Date().time,
                    wordArg,
                    null,
                    null,
                    translate,
                    0f,
                    true
                )
            }
            wordRepository.saveWord(wordDBEntity)
            launch(Dispatchers.Main) {
                viewState.sendSuccessResult()
                viewState.completeProgressDialog()
                router.exit()
            }
        }
    }

    fun onDeleteWord() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startProgressDialog()
            }
            wordRepository.deleteWordById(word?.id ?: 0)
            launch(Dispatchers.Main) {
                viewState.sendSuccessResult()
                viewState.completeProgressDialog()
                router.exit()
            }
        }
    }

    fun setValidationFields(vararg observables: Observable<CharSequence>) {
        val wordObservable = observables[0]
        val translateObservable = observables[1]
        disposable = Observable.combineLatest(
            wordObservable, translateObservable,
            { word: CharSequence, translate: CharSequence ->
                word.toString().trim().isNotEmpty() && translate.toString().trim().isNotEmpty()
            }
        )
            .subscribe { isEnable: Boolean -> viewState.enableSaveButton(isEnable) }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
