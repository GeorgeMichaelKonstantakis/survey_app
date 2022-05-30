package com.gkonstantakis.survey.data.network

import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.network.entities.AnswerNetworkEntity
import com.gkonstantakis.survey.data.network.entities.QuestionsNetworkEntity
import retrofit2.http.GET
import retrofit2.http.POST

interface QuestionsNetworkService {

    @GET("questions")
    suspend fun getQuestions(): QuestionsNetworkEntity

    @POST("question/submit")
    suspend fun postAnswer(answerNetworkEntity: AnswerNetworkEntity)
}