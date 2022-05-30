package com.gkonstantakis.survey.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gkonstantakis.survey.data.database.entities.AnswerCacheEntity
import com.gkonstantakis.survey.data.database.entities.QuestionCacheEntity

@Database(entities = [QuestionCacheEntity::class, AnswerCacheEntity::class], version = 1)
abstract class SurveyDatabase : RoomDatabase() {

    abstract fun surveyDao(): SurveyDao

    companion object {
        const val SURVEY_DATABASE: String = "survey_db"
    }
}