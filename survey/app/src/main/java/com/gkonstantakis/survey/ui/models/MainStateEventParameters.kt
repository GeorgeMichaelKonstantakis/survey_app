package com.gkonstantakis.survey.ui.models

import com.gkonstantakis.survey.data.models.Answer
import com.gkonstantakis.survey.data.models.Question

data class MainStateEventParameters(
    var answer: Answer
) {
}