package team.ommaya.wequiz.android.domain.model.quiz

data class QuizList(
    val quiz: List<Quiz>,
    val nextCursor: Int,
)
