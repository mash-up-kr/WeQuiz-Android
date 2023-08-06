/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.model.user

data class UserInformation(
    val code: String,
    val data: UserData,
    val message: String,
)

data class UserData(
    val id: Int,
    val nickname: String,
    val description: String,
)
