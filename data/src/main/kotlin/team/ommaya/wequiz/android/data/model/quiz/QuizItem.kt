package team.ommaya.wequiz.android.data.model.quiz

import com.fasterxml.jackson.annotation.JsonProperty

data class QuizItem(
    @field:JsonProperty("id")
    val id: Int? = null,

    @field:JsonProperty("title")
    val title: String? = null,
)
