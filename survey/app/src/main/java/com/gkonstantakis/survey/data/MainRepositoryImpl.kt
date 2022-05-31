package com.gkonstantakis.survey.data

import com.gkonstantakis.survey.data.database.SurveyDao
import com.gkonstantakis.survey.data.mapping.AnswerCacheMapper
import com.gkonstantakis.survey.data.mapping.AnswerNetworkMapper
import com.gkonstantakis.survey.data.mapping.QuestionCacheMapper
import com.gkonstantakis.survey.data.mapping.QuestionNetworkMapper
import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.models.Question
import com.gkonstantakis.survey.data.network.QuestionsNetworkService
import com.gkonstantakis.survey.data.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.Exception

class MainRepositoryImpl(
    private val surveyDao: SurveyDao,
    private val questionsNetworkService: QuestionsNetworkService,
    private val answerCacheMapper: AnswerCacheMapper,
    private val answerNetworkMapper: AnswerNetworkMapper,
    private val questionCacheMapper: QuestionCacheMapper,
    private val questionNetworkMapper: QuestionNetworkMapper,
) : MainRepository {

    override suspend fun getQuestions(): Flow<DataState<List<Question>>> = flow {
        emit(DataState.Loading)
        try {
            surveyDao.deleteQuestions()
            val networkQuestions = questionsNetworkService.getQuestions()
            val questions = questionNetworkMapper.mapFromListOfEntities(networkQuestions)
            emit(DataState.SuccessNetworkQuestions(questions))
        } catch (e1: Exception) {
            try {
                val databaseQuestions = surveyDao.getQuestions()
                val questions = questionCacheMapper.mapFromListOfEntities(databaseQuestions)
                emit(DataState.SuccessDatabaseQuestions(questions))
            } catch (e2: Exception) {
                emit(DataState.Error(e1))
            }
        }
    }

    override suspend fun getAnswers(): Flow<DataState<List<Answer>>> = flow {
        emit(DataState.Loading)
        try {
            val databaseAnswers = surveyDao.getAnswers()
            val answers = answerCacheMapper.mapFromListOfEntities(databaseAnswers)
            emit(DataState.SuccessDatabaseAnswers(answers))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun postAnswer(answer: Answer) {
        var requestSuccessful = false
        try {
            val networkAnswer = answerNetworkMapper.mapToEntity(answer)
            questionsNetworkService.postAnswer(networkAnswer)
            requestSuccessful = true
        } catch (e: Exception) {

        }

        if (requestSuccessful) {
            try {
                val databaseAnswer = answerCacheMapper.mapToEntity(answer)
                surveyDao.insertAnswer(databaseAnswer)
            } catch (e: Exception) {

            }
        }
    }

    override suspend fun insertQuestion(question: Question) {
        try{
            val databaseQuestion = questionCacheMapper.mapToEntity(question)
            surveyDao.insertQuestion(databaseQuestion)
        } catch (e: Exception){

        }
    }

    override suspend fun deleteQuestions() {
        try {
            surveyDao.deleteQuestions()
        } catch (e: Exception) {

        }
    }

    override suspend fun deleteAnswers() {
        try {
            surveyDao.deleteAnswers()
        } catch (e: Exception) {

        }
    }
}