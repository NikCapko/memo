package com.nik.capko.memo.ui.dictionary

import com.nik.capko.memo.data.Dictionary
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.repository.DictionaryRepository
import com.nik.capko.memo.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject

class DictionaryPresenter @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : MvpPresenter<DictionaryView>() {

    private var dictionaryList = emptyList<Dictionary>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadDictionaryList()
    }

    private fun loadDictionaryList() {
        CoroutineScope(Dispatchers.Default).launch {
            launch(Dispatchers.Main) {
                viewState.startLoading()
            }
            delay(2000)
            val resource = dictionaryRepository.getDictionaryList()
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
                viewState.startLoading()
            }
            delay(2000)

            val resource: Resource<List<Word>> = dictionaryList.getOrNull(position)?.id?.let {
                dictionaryRepository.getDictionary(it)
            } ?: Resource.error("Error", null)
            checkDictionaryResponse(resource)
        }
    }

    private fun CoroutineScope.checkDictionaryResponse(resource: Resource<List<Word>>) = launch {
        when (resource.status) {
            Resource.Status.SUCCESS -> {
                val words = resource.data as List<Word>
            }
            Resource.Status.ERROR -> {
                launch(Dispatchers.Main) {
                    viewState.errorLoading(resource.message)
                }
            }
        }
    }
}
