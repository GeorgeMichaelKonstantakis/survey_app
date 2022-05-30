package com.gkonstantakis.survey.data.utils

sealed class DataState<out R> {

    data class SuccessNetworkQuestions<out T>(val data: T) : DataState<T>()

    data class SuccessDatabaseQuestions<out T>(val data: T) : DataState<T>()

    data class SuccessDatabaseAnswers<out T>(val data: T) : DataState<T>()

    data class Error(val exception: Exception) : DataState<Nothing>()

    object Loading : DataState<Nothing>()
}