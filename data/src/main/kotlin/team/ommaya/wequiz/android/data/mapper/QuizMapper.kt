/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.mapper

import team.ommaya.wequiz.android.data.model.quiz.OptionDto
import team.ommaya.wequiz.android.data.model.quiz.QuestionDto
import team.ommaya.wequiz.android.domain.model.quiz.Option
import team.ommaya.wequiz.android.domain.model.quiz.Question

fun List<Option>.toOptionDtoList(): List<OptionDto> {
    return this.map {
        OptionDto(
            content = it.content,
            priority = it.priority,
            isCorrect = it.isCorrect,
        )
    }
}

fun List<Question>.toQuestionDtoList(): List<QuestionDto> {
    return this.map {
        QuestionDto(
            title = it.title,
            priority = it.priority,
            duplicatedOption = it.duplicatedOption,
            options = it.options.toOptionDtoList()
        )
    }
}
