package com.gkonstantakis.survey.data.mapping

import com.gkonstantakis.survey.data.database.entities.AnswerCacheEntity
import com.gkonstantakis.survey.data.models.Answer

class AnswerCacheMapper {

    fun mapFromEntity(entity: AnswerCacheEntity): Answer {
        return Answer(
            id = entity.id,
            answer = entity.answer
        )
    }

    fun mapToEntity(entity: Answer): AnswerCacheEntity {
        return AnswerCacheEntity(
            id = entity.id,
            answer = entity.answer
        )
    }

    fun mapFromListOfEntities(entities: List<AnswerCacheEntity>): List<Answer> {
        return entities.map {
            mapFromEntity(it)
        }
    }

    fun mapToListOfEntities(entities: List<Answer>): List<AnswerCacheEntity> {
        return entities.map {
            mapToEntity(it)
        }
    }
}