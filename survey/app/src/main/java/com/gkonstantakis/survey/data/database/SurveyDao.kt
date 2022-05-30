package com.gkonstantakis.survey.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gkonstantakis.survey.data.database.entities.AnswerCacheEntity
import com.gkonstantakis.survey.data.database.entities.QuestionCacheEntity

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionCacheEntity: QuestionCacheEntity): Long

    @Query("SELECT * FROM QUESTIONS")
    suspend fun getQuestions(): List<QuestionCacheEntity>

    @Query("DELETE FROM QUESTIONS")
    suspend fun deleteQuestions(): List<QuestionCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answerCacheEntity: AnswerCacheEntity): Long

    @Query("SELECT * FROM ANSWERS")
    suspend fun getAnswers(): List<AnswerCacheEntity>

    @Query("DELETE FROM ANSWERS")
    suspend fun deleteAnswers(): List<AnswerCacheEntity>
}