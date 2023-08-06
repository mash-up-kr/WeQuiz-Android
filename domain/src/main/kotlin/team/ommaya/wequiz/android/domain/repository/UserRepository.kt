/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.repository

import team.ommaya.wequiz.android.domain.model.user.User
import team.ommaya.wequiz.android.domain.model.user.UserInformation

interface UserRepository {
    suspend fun getUser(): User
    suspend fun getInformation(token: String): UserInformation
    suspend fun getAnonymousToken(nickname: String): String
    suspend fun postUserJoin(
        token: String,
        phone: String,
        nickname: String,
        description: String,
    ): String

    suspend fun saveUserToken(token: String)
}
