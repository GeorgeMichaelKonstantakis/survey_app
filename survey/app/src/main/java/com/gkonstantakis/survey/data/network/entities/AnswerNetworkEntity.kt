package com.gkonstantakis.survey.data.network.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnswerNetworkEntity(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("answer")
    @Expose
    var answer: String,
) {
}