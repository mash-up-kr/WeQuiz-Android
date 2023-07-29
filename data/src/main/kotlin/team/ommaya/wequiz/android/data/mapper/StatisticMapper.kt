/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.mapper

import team.ommaya.wequiz.android.data.model.statistic.QuizStatisticResponse
import team.ommaya.wequiz.android.domain.model.statistic.Options
import team.ommaya.wequiz.android.domain.model.statistic.Questions
import team.ommaya.wequiz.android.domain.model.statistic.QuizStatistic

internal fun QuizStatisticResponse.toDomain() =
    QuizStatistic(
        quizId = requireNotNull(quizId),
        questions = requireNotNull(questions)
            .map { question ->
                requireNotNull(question)
                Questions(
                    questionId = requireNotNull(question.questionId),
                    questionTitle = requireNotNull(question.questionTitle),
                    options = requireNotNull(question.options)
                        .map { option ->
                            requireNotNull(option)
                            Options(
                                selectivity = requireNotNull(option.selectivity),
                                optionId = requireNotNull(option.optionId),
                                content = requireNotNull(option.content),
                                isCorrect = requireNotNull(option.isCorrect),
                            )
                        },
                )
            },
        quizTitle = requireNotNull(quizTitle),
    )
