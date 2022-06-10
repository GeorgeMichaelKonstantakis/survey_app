package com.gkonstantakis.survey.ui.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkonstantakis.survey.data.MainRepositoryImpl
import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.models.Question
import com.gkonstantakis.survey.data.utils.DataState
import com.gkonstantakis.survey.ui.models.MainStateEventParameters
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepositoryImpl
) : ViewModel() {

    var pageCount: MutableLiveData<Int> = MutableLiveData(0)

    private var _questionDataState: MutableLiveData<DataState<List<Question>>> = MutableLiveData()

    val questionDataState: LiveData<DataState<List<Question>>>
        get() = _questionDataState

    private val _answerDataState: MutableLiveData<DataState<List<Answer>>> = MutableLiveData()

    val answerDataState: LiveData<DataState<List<Answer>>>
        get() = _answerDataState

    var _postAnswerState: MutableLiveData<DataState<List<Answer>>> = MutableLiveData()
    val postAnswerState: LiveData<DataState<List<Answer>>>
        get() = _postAnswerState

    fun setDataStateEvent(
        mainStateEvent: MainStateEvent,
        mainStateEventParameters: MainStateEventParameters?
    ) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetQuestionEvent -> {
                    mainRepository.getQuestions().onEach {
                        _questionDataState.value = it
                    }.launchIn(viewModelScope)
                }
                is MainStateEvent.GetAnswersEvent -> {
                    mainRepository.getAnswers().onEach {
                        _answerDataState.value = it
                    }.launchIn(viewModelScope)
                }
                is MainStateEvent.PostAnswerEvent -> {
                    if (mainStateEventParameters != null) {
                        mainRepository.postAnswer(mainStateEventParameters.answer).onEach {
                            _postAnswerState.value = it
                        }.launchIn(viewModelScope)
                    }
                }
            }
        }
    }

    fun setPageCount(count: Int) {
        viewModelScope.launch {
            pageCount.value = count
        }
    }
}


sealed class MainStateEvent {
    object GetQuestionEvent : MainStateEvent()

    object GetAnswersEvent : MainStateEvent()

    object PostAnswerEvent : MainStateEvent()
}