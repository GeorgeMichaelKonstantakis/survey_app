package com.gkonstantakis.survey.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gkonstantakis.survey.data.database.entities.AnswerCacheEntity

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answerCacheEntity: AnswerCacheEntity): Long

    @Query("SELECT * FROM ANSWERS")
    suspend fun getAnswers(): List<AnswerCacheEntity>
}