package com.gkonstantakis.survey.data

import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.models.Question
import com.gkonstantakis.survey.data.utils.DataState
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getQuestions(): Flow<DataState<List<Question>>>

    suspend fun getAnswers(): Flow<DataState<List<Answer>>>

    suspend fun postAnswer(answer: Answer): Flow<DataState<List<Answer>>>
}