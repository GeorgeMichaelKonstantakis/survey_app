package com.gkonstantakis.survey.data.utils

sealed class DataState<out R> {

    data class SuccessNetworkQuestions<out T>(val data: T) : DataState<T>()

    data class SuccessDatabaseAnswers<out T>(val data: T) : DataState<T>()

    data class Error(val exception: Exception) : DataState<Nothing>()

    data class SuccessPostAnswer<out T>(val data: T) : DataState<T>()

    data class ErrorPostAnswer<out T>(val data: T) : DataState<T>()

    object Loading : DataState<Nothing>()
}