package com.gkonstantakis.survey.data.mapping

import com.gkonstantakis.survey.data.models.Question
import com.gkonstantakis.survey.data.network.entities.QuestionNetworkEntity

class QuestionNetworkMapper {
    fun mapFromEntity(entity: QuestionNetworkEntity): Question {
        return Question(
            id = entity.id,
            question = entity.question
        )
    }

    fun mapToEntity(entity: Question): QuestionNetworkEntity {
        return QuestionNetworkEntity(
            id = entity.id,
            question = entity.question
        )
    }

    fun mapFromListOfEntities(entities: List<QuestionNetworkEntity>): List<Question> {
        return entities.map {
            mapFromEntity(it)
        }
    }
}