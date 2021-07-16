package com.nikcapko.memo.ui.dictionary

import com.github.terrakok.cicerone.Router
import com.nikcapko.core.network.Resource
import com.nikcapko.memo.data.Dictionary
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.usecases.DictionaryListUseCase
import com.nikcapko.memo.usecases.DictionaryUseCase
import com.nikcapko.memo.usecases.SaveWordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject

class DictionaryPresenter @Inject constructor(
    private val router: Router,
    private val dictionaryListUseCase: DictionaryListUseCase,
    private val dictionaryUseCase: DictionaryUseCase,
    private val saveWordUseCase: SaveWordUseCase,
) : MvpPresenter<DictionaryView>() {

    private var dictionaryList = emptyList<Dictionary>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadDictionaryList()
    }

    fun loadDictionaryList() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }
            val resource = dictionaryListUseCase.getDictionaryList()
            checkDictionaryListResponse(resource)
        }
    }

    private fun CoroutineScope.checkDictionaryListResponse(resource: Resource<List<Dictionary>>) =
        launch {
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    dictionaryList = resource.data as List<Dictionary>
                    launch(Dispatchers.Main) {
                        viewState.showDictionaryList(dictionaryList)
                        viewState.completeLoading()
                    }
                }
                Resource.Status.ERROR -> {
                    launch(Dispatchers.Main) {
                        viewState.errorLoading(resource.message)
                    }
                }
            }
        }

    fun onItemClick(position: Int) {
        viewState.showLoadingDialog(position)
    }

    fun loadDictionary(position: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startProgressDialog()
            }
            val resource: Resource<List<Word>> = dictionaryList.getOrNull(position)?.id?.let {
                dictionaryUseCase.getDictionary(it)
            } ?: Resource.error("Error", null)
            checkDictionaryResponse(resource)
        }
    }

    private fun CoroutineScope.checkDictionaryResponse(resource: Resource<List<Word>>) =
        launch {
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    val words = resource.data as List<Word>
                    words.forEach { word ->
                        saveWordUseCase.saveWord(word)
                        word.forms.forEach { form -> saveWordUseCase.saveForm(form) }
                    }
                    launch(Dispatchers.Main) {
                        viewState.completeProgressDialog()
                        viewState.sendSuccessResult()
                        router.exit()
                    }
                }
                Resource.Status.ERROR -> {
                    launch(Dispatchers.Main) {
                        viewState.errorProgressDialog(resource.message)
                    }
                }
            }
        }
}
