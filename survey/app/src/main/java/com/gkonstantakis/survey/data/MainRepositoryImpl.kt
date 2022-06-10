package com.gkonstantakis.survey.data

import android.util.Log
import com.gkonstantakis.survey.data.database.SurveyDao
import com.gkonstantakis.survey.data.mapping.AnswerCacheMapper
import com.gkonstantakis.survey.data.mapping.AnswerNetworkMapper
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
    private val questionNetworkMapper: QuestionNetworkMapper,
) : MainRepository {

    override suspend fun getQuestions(): Flow<DataState<List<Question>>> = flow {
        emit(DataState.Loading)
        try {
            val networkQuestions = questionsNetworkService.getQuestions()
            val questions = questionNetworkMapper.mapFromListOfEntities(networkQuestions)
            for (y in questions) {
                Log.e("getQuestions", "Mapper -> no ${y.id}: ${y?.question}")
            }
            emit(DataState.SuccessNetworkQuestions(questions))
        } catch (e: Exception) {
            Log.e("getQuestions", "ERROR: " + e.toString())
            emit(DataState.Error(e))
        }
    }

    override suspend fun getAnswers(): Flow<DataState<List<Answer>>> = flow {
        emit(DataState.Loading)
        try {
            val databaseAnswers = surveyDao.getAnswers()
            val answers = answerCacheMapper.mapFromListOfEntities(databaseAnswers)
            emit(DataState.SuccessDatabaseAnswers(answers))
        } catch (e: Exception) {
            Log.e("getAnswers", "ERROR: " + e.toString())
            emit(DataState.Error(e))
        }
    }

    override suspend fun postAnswer(answer: Answer): Flow<DataState<List<Answer>>> = flow {
        try {
            val networkAnswer = answerNetworkMapper.mapToEntity(answer)
            questionsNetworkService.postAnswer(networkAnswer)
            val databaseAnswer = answerCacheMapper.mapToEntity(answer)
            surveyDao.insertAnswer(databaseAnswer)
            var answers: ArrayList<Answer> = ArrayList()
            answers.add(answer)
            emit(DataState.SuccessPostAnswer(answers))
        } catch (e: Exception) {
            var answers: ArrayList<Answer> = ArrayList()
            answers.add(answer)
            emit(DataState.ErrorPostAnswer(answers))
            Log.e("postAnswer", "ERROR: " + e.toString())
        }
    }
}