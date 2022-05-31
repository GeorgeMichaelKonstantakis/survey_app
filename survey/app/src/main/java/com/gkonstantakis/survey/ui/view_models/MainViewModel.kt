package com.gkonstantakis.survey.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkonstantakis.survey.data.MainRepositoryImpl
import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.models.Question
import com.gkonstantakis.survey.data.utils.DataState
import com.gkonstantakis.survey.ui.models.MainStateEventParameters
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepositoryImpl
) : ViewModel() {

    private var _questionDataState: MutableLiveData<DataState<List<Question>>> = MutableLiveData()

    val questionDataState: LiveData<DataState<List<Question>>>
        get() = _questionDataState

    private val _answerDataState: MutableLiveData<DataState<List<Answer>>> = MutableLiveData()

    val answerDataState: LiveData<DataState<List<Answer>>>
        get() = _answerDataState


    fun setStateEvent(
        mainStateEvent: MainStateEvent,
        mainStateEventParameters: MainStateEventParameters
    ) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetQuestionEvent -> {
                    mainRepository.getQuestions().onEach {
                        _questionDataState.value = it
                    }
                }
                is MainStateEvent.GetAnswersEvent -> {
                    mainRepository.getAnswers().onEach {
                        _answerDataState.value = it
                    }
                }
                is MainStateEvent.PostAnswerEvent -> {
                    mainRepository.postAnswer(mainStateEventParameters.answer)
                }
                is MainStateEvent.InsertQuestionEvent -> {
                    mainRepository.insertQuestion(mainStateEventParameters.question)
                }
                is MainStateEvent.DeleteQuestionsEvent -> {
                    mainRepository.deleteQuestions()
                }
                is MainStateEvent.DeleteAnswersEvent -> {
                    mainRepository.deleteAnswers()
                }
            }
        }
    }
}


sealed class MainStateEvent {
    object GetQuestionEvent : MainStateEvent()

    object GetAnswersEvent : MainStateEvent()

    object PostAnswerEvent : MainStateEvent()

    object InsertQuestionEvent : MainStateEvent()

    object DeleteQuestionsEvent : MainStateEvent()

    object DeleteAnswersEvent : MainStateEvent()

    object None : MainStateEvent()
}