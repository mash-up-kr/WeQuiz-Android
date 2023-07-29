/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.model.statistic

data class Options(
    val selectivity: Float,
    val optionId: Int,
    val content: String,
    val isCorrect: Boolean,
)
