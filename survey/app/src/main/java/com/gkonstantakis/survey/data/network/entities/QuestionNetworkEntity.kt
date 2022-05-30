package com.gkonstantakis.survey.data.network.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuestionNetworkEntity(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("question")
    @Expose
    var question: String,
) {
}