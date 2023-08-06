package com.nikcapko.memo.ui.words.detail

import com.github.terrakok.cicerone.Router
import com.nikcapko.domain.usecases.DeleteWordUseCase
import com.nikcapko.domain.usecases.SaveWordUseCase
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.mapper.WordModelMapper
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
    private val saveWordUseCase: SaveWordUseCase,
    private val deleteWordUseCase: DeleteWordUseCase,
    private val wordModelMapper: WordModelMapper,
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
        CoroutineScope(Dispatchers.IO).launch {
            launch(Dispatchers.Main) {
                viewState.startProgressDialog()
            }
            val word: Word = word?.let {
                it.apply {
                    word = wordArg
                    translation = translate
                }
            } ?: run {
                Word(
                    id = Date().time,
                    word = wordArg,
                    translation = translate,
                    frequency = 0f,
                )
            }
            saveWordUseCase(wordModelMapper.mapToEntity(word))
            launch(Dispatchers.Main) {
                viewState.sendSuccessResult()
                viewState.completeProgressDialog()
                router.exit()
            }
        }
    }

    fun onDeleteWord() {
        CoroutineScope(Dispatchers.IO).launch {
            launch(Dispatchers.Main) {
                viewState.startProgressDialog()
            }
            word?.let { deleteWordUseCase(it.id.toString()) }
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
            wordObservable, translateObservable
        ) { word: CharSequence, translate: CharSequence ->
            word.toString().trim().isNotEmpty() && translate.toString().trim().isNotEmpty()
        }
            .subscribe { isEnable: Boolean -> viewState.enableSaveButton(isEnable) }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
