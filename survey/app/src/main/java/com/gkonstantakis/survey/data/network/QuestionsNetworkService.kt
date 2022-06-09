package com.gkonstantakis.survey.data.network

import com.gkonstantakis.survey.data.network.entities.AnswerNetworkEntity
import com.gkonstantakis.survey.data.network.entities.QuestionNetworkEntity
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface QuestionsNetworkService {

    @GET("questions")
    suspend fun getQuestions(): List<QuestionNetworkEntity>

    @POST("question/submit")
    suspend fun postAnswer(@Body answerNetworkEntity: AnswerNetworkEntity)
}