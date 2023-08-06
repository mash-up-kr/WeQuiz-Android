/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import team.ommaya.wequiz.android.domain.model.quiz.Option
import java.security.SecureRandom
import team.ommaya.wequiz.android.domain.model.quiz.Question as QuestionDomainModel

data class Question(
    val key: Long = 0,
    val title: String = "문제입력",
    val isFocus: Boolean = false,
    val isMultipleChoice: Boolean = false,
    val type: QuestionType = QuestionType.Default,
    val answerList: List<Answer> = Answer.getInitialAnswerList(),
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

    companion object {
        fun makeQuestion(type: QuestionType = QuestionType.Default) =
            Question(
                key = SecureRandom().nextLong(),
                type = type,
            )

        fun getInitialQuestionList() = listOf<Question>(
            makeQuestion(),
            makeQuestion(),
            makeQuestion(QuestionType.Add),
        )
    }
}

data class Answer(
    val key: Long = 0,
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

    companion object {
        fun makeAnswer(type: AnswerType = AnswerType.Default) =
            Answer(
                key = SecureRandom().nextLong(),
                type = type,
            )

        fun getInitialAnswerList() = listOf<Answer>(
            makeAnswer(),
            makeAnswer(),
            makeAnswer(AnswerType.Add),
        )
    }
}

fun List<Answer>.toOptionList(): List<Option> {
    val list = mutableListOf<Option>()
    this.forEachIndexed { index, answer ->
        list.add(
            Option(
                content = answer.content,
                id = index,
                isCorrect = answer.isCorrect,
            ),
        )
    }
    return list.toList()
}

fun List<Question>.toQuestionDomainList(): List<QuestionDomainModel> {
    val list = mutableListOf<QuestionDomainModel>()
    this.forEachIndexed { index, question ->
        list.add(
            QuestionDomainModel(
                title = question.title,
                id = index,
                duplicatedOption = question.isMultipleChoice,
                options = question.answerList.toOptionList(),
            ),
        )
    }
    return list.toList()
}
