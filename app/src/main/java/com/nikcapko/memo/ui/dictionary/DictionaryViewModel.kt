package com.nikcapko.memo.ui.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.nikcapko.core.network.Resource
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.core.viewmodel.DataSendingViewModelState
import com.nikcapko.memo.data.Dictionary
import com.nikcapko.memo.data.Word
import com.nikcapko.memo.usecases.DictionaryListUseCase
import com.nikcapko.memo.usecases.DictionaryUseCase
import com.nikcapko.memo.usecases.SaveWordUseCase
import com.nikcapko.memo.utils.extensions.default
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ar2code.mutableliveevent.EventArgs
import ru.ar2code.mutableliveevent.MutableLiveEvent
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val router: Router,
    private val dictionaryListUseCase: DictionaryListUseCase,
    private val dictionaryUseCase: DictionaryUseCase,
    private val saveWordUseCase: SaveWordUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<DataLoadingViewModelState>().default(initialValue = DataLoadingViewModelState.LoadingState)
    val dataLoadingViewModelState: LiveData<DataLoadingViewModelState>
        get() = _state

    private val _dictionaryLoadState = MutableLiveData<DataSendingViewModelState>().default(initialValue = DataSendingViewModelState.SentState)
    val dictionaryLoadingViewModelState: LiveData<DataSendingViewModelState>
        get() = _dictionaryLoadState

    private val _loadDictionaryDialogEvent = MutableLiveEvent<LoadDictionaryDialogEvent>()
    val loadDictionaryDialogEvent: LiveData<LoadDictionaryDialogEvent>
        get() = _loadDictionaryDialogEvent

    private var dictionaryList = emptyList<Dictionary>()

    init {
        loadDictionaryList()
    }

    fun loadDictionaryList() {
        viewModelScope.launch {
            launch(Dispatchers.Main) {
                _state.postValue(DataLoadingViewModelState.LoadingState)
            }
            val resource = dictionaryListUseCase.getDictionaryList()
            checkDictionaryListResponse(resource)
        }
    }

    private fun checkDictionaryListResponse(resource: Resource<List<Dictionary>>) =
        viewModelScope.launch {
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    dictionaryList = (resource.data as? List<*>)?.filterIsInstance<Dictionary>().orEmpty()
                    launch(Dispatchers.Main) {
                        _state.postValue(DataLoadingViewModelState.LoadedState(dictionaryList))
                    }
                }
                Resource.Status.ERROR -> {
                    launch(Dispatchers.Main) {
                        _state.postValue(DataLoadingViewModelState.ErrorState(Error(resource.message)))
                    }
                }
            }
        }

    fun onItemClick(position: Int) {
        _loadDictionaryDialogEvent.postValue(LoadDictionaryDialogEvent(position))
    }

    fun loadDictionary(position: Int) {
        viewModelScope.launch {
            launch(Dispatchers.Main) {
                _dictionaryLoadState.postValue(DataSendingViewModelState.SendingState)
            }
            val resource: Resource<List<Word>> = dictionaryList.getOrNull(position)?.id?.let {
                dictionaryUseCase.getDictionary(it)
            } ?: Resource.error("Error", null)
            checkDictionaryResponse(resource)
        }
    }

    private fun checkDictionaryResponse(resource: Resource<List<Word>>) =
        viewModelScope.launch {
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    val words = (resource.data as? List<*>)?.filterIsInstance<Word>().orEmpty()
                    words.forEach { word ->
                        saveWordUseCase.saveWord(word)
                        word.forms.forEach { form -> saveWordUseCase.saveForm(form) }
                    }
                    launch(Dispatchers.Main) {
                        _dictionaryLoadState.postValue(DataSendingViewModelState.SentState)
                        router.exit()
                    }
                }
                Resource.Status.ERROR -> {
                    launch(Dispatchers.Main) {
                        _dictionaryLoadState.postValue(DataSendingViewModelState.ErrorState(Error(resource.message)))
                    }
                }
            }
        }

    class LoadDictionaryDialogEvent(position: Int) : EventArgs<Int>(position)
}