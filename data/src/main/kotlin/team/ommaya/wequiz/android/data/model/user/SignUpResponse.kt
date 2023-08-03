/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val code: String = "",
    val data: Boolean = false,
    val message: String = "",
)
