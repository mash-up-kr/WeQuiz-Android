package team.ommaya.wequiz.android.data.model.quiz

import com.fasterxml.jackson.annotation.JsonProperty

data class QuizListResponse(
    @field:JsonProperty("quiz")
    val quiz: List<QuizItem?>? = null,

    @field:JsonProperty("nextCursor")
    val nextCursor: Int? = null,
)
