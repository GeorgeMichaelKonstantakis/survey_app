package com.gkonstantakis.survey.data.mapping

import com.gkonstantakis.survey.data.database.entities.QuestionCacheEntity
import com.gkonstantakis.survey.data.models.Question

class QuestionCacheMapper {

    fun mapFromEntity(entity: QuestionCacheEntity): Question {
        return Question(
            id = entity.id,
            question = entity.question
        )
    }

    fun mapToEntity(entity: Question): QuestionCacheEntity {
        return QuestionCacheEntity(
            id = entity.id,
            question = entity.question
        )
    }

    fun mapFromListOfEntities(entities: List<QuestionCacheEntity>): List<Question> {
        return entities.map {
            mapFromEntity(it)
        }
    }

    fun mapToListOfEntities(entities: List<Question>): List<QuestionCacheEntity> {
        return entities.map {
            mapToEntity(it)
        }
    }
}