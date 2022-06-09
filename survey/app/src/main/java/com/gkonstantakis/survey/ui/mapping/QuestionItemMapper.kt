package com.gkonstantakis.survey.ui.mapping

import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.models.Question
import com.gkonstantakis.survey.ui.models.QuestionListItem

class QuestionItemMapper {

    fun mapFromDomainAnswer(entity: Answer): QuestionListItem {
        return QuestionListItem(
            id = entity.id,
            question = null,
            answer = entity.answer
        )
    }

    fun mapFromDomainQuestion(entity: Question): QuestionListItem {
        return QuestionListItem(
            id = entity.id,
            question = entity.question,
            answer = null
        )
    }

    fun mapToDomainAnswer(entity: QuestionListItem): Answer {
        return Answer(
            id = entity.id,
            answer = entity.answer!!
        )
    }

    fun mapToListOfDomainAnswers(entities: List<QuestionListItem>): List<Answer> {
        return entities.map {
            mapToDomainAnswer(it)
        }
    }
}