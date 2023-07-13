/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

data class Question(
    val index: Int = 0,
    val title: String = "문제입력",
    val isFocus: Boolean = false,
    val isMultipleChoice: Boolean = false,
    val type: QuestionType = QuestionType.Default,
    val answerList: List<Answer> = listOf(
        Answer(index = 0),
        Answer(index = 1),
        Answer(index = -1, type = Answer.AnswerType.Add),
    ),
) {
    sealed interface QuestionType {
        val typeNum: Int

        object Default : QuestionType {
            override val typeNum = 0
        }

        object Add : QuestionType {
            override val typeNum = 1
        }
    }
}

data class Answer(
    val index: Int = 0,
    val content: String = "",
    val isCorrect: Boolean = false,
    val type: AnswerType = AnswerType.Default,
) {
    sealed interface AnswerType {
        val typeNum: Int

        object Default : AnswerType {
            override val typeNum: Int = 0
        }

        object Add : AnswerType {
            override val typeNum: Int = 1
        }
    }
}
