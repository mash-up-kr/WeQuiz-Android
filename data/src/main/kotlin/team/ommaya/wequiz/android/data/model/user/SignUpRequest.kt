/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val token: String = "",
    val phone: String = "",
    val nickname: String = "",
    val description: String = "",
)
