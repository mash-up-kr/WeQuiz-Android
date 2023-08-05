/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.mapper

import team.ommaya.wequiz.android.data.model.quiz.OptionDto
import team.ommaya.wequiz.android.data.model.quiz.QuestionDto
import team.ommaya.wequiz.android.data.model.quiz.QuizDetailResponse
import team.ommaya.wequiz.android.data.model.quiz.QuizListResponse
import team.ommaya.wequiz.android.data.model.quiz.SubmitResult
import team.ommaya.wequiz.android.domain.model.quiz.Creator
import team.ommaya.wequiz.android.domain.model.quiz.Option
import team.ommaya.wequiz.android.domain.model.quiz.Question
import team.ommaya.wequiz.android.domain.model.quiz.Quiz
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetail
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailOption
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailQuestion
import team.ommaya.wequiz.android.domain.model.quiz.QuizList
import team.ommaya.wequiz.android.domain.model.quiz.QuizResult

internal fun List<Option>.toOptionDtoList(): List<OptionDto> {
    return this.map {
        OptionDto(
            content = it.content,
            id = it.id,
            isCorrect = it.isCorrect,
        )
    }
}

internal fun List<Question>.toQuestionDtoList(): List<QuestionDto> {
    return this.map {
        QuestionDto(
            title = it.title,
            id = it.id,
            duplicatedOption = it.duplicatedOption,
            options = it.options.toOptionDtoList(),
        )
    }
}

internal fun QuizListResponse.toDomain() =
    QuizList(
        quiz = requireNotNull(quiz)
            .map { quizItem ->
                requireNotNull(quizItem)
                Quiz(
                    id = requireNotNull(quizItem.id),
                    title = requireNotNull(quizItem.title),
                )
            },
        nextCursor = nextCursor,
    )

internal fun QuizDetailResponse.toDomain() =
    QuizDetail(
        questions = requireNotNull(questions)
            .map { question ->
                requireNotNull(question)
                QuizDetailQuestion(
                    score = requireNotNull(question.score),
                    answerCounts = requireNotNull(question.answerCounts),
                    options = requireNotNull(question.options)
                        .map { option ->
                            requireNotNull(option)
                            QuizDetailOption(
                                id = requireNotNull(option.id),
                                content = requireNotNull(option.content),
                                isCorrect = requireNotNull(option.isCorrect),
                            )
                        },
                    id = requireNotNull(question.id),
                    title = requireNotNull(question.title),
                )
            },
        id = requireNotNull(id),
        title = requireNotNull(title),
        creator = Creator(creator.id, creator.name)
    )

internal fun SubmitResult.toQuizResult(): QuizResult {
    return QuizResult(
        score = totalScore,
        creatorId = quizCreator.id,
        creatorName = quizCreator.name,
        resolverId = quizResolver.id,
        resolverName = quizResolver.name,
    )
}