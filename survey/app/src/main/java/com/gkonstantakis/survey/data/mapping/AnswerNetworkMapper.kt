package com.gkonstantakis.survey.data.mapping

import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.network.entities.AnswerNetworkEntity

class AnswerNetworkMapper {

    fun mapFromEntity(entity: AnswerNetworkEntity): Answer {
        return Answer(
            id = entity.id,
            answer = entity.answer
        )
    }

    fun mapToEntity(entity: Answer): AnswerNetworkEntity {
        return AnswerNetworkEntity(
            id = entity.id,
            answer = entity.answer
        )
    }

    fun mapFromListOfEntities(entities: List<AnswerNetworkEntity>): List<Answer> {
        return entities.map {
            mapFromEntity(it)
        }
    }

    fun mapToListOfEntities(entities: List<Answer>): List<AnswerNetworkEntity> {
        return entities.map {
            mapToEntity(it)
        }
    }
}