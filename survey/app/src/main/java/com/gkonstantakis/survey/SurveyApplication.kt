package com.gkonstantakis.survey

import android.app.Application
import androidx.room.Room
import com.gkonstantakis.survey.data.MainRepositoryImpl
import com.gkonstantakis.survey.data.database.SurveyDao
import com.gkonstantakis.survey.data.database.SurveyDatabase
import com.gkonstantakis.survey.data.mapping.AnswerCacheMapper
import com.gkonstantakis.survey.data.mapping.AnswerNetworkMapper
import com.gkonstantakis.survey.data.mapping.QuestionCacheMapper
import com.gkonstantakis.survey.data.mapping.QuestionNetworkMapper
import com.gkonstantakis.survey.data.network.Constant
import com.gkonstantakis.survey.data.network.QuestionsNetworkService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SurveyApplication : Application() {

    lateinit var surveyDB: SurveyDatabase
    lateinit var surveyDao: SurveyDao
    lateinit var questionsNetworkService: QuestionsNetworkService
    lateinit var mainRepository: MainRepositoryImpl

    override fun onCreate() {
        super.onCreate()

        surveyDB = Room
            .databaseBuilder(
                applicationContext,
                SurveyDatabase::class.java,
                SurveyDatabase.SURVEY_DATABASE
            )
            .build()

        surveyDao = (surveyDB as SurveyDatabase).surveyDao()

        questionsNetworkService = provideGsonBuilder().let {
            provideNetwork(it).build().create(QuestionsNetworkService::class.java)
        }

        mainRepository = questionsNetworkService.let {
            MainRepositoryImpl(
                surveyDao,
                it, AnswerCacheMapper(), AnswerNetworkMapper(), QuestionCacheMapper(),
                QuestionNetworkMapper()
            )
        }
    }

    fun provideGsonBuilder(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    fun provideNetwork(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

}