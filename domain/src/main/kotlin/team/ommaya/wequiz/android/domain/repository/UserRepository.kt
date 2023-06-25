/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.repository

import kotlinx.coroutines.flow.Flow
import team.ommaya.wequiz.android.domain.model.User

interface UserRepository {

    fun getUser(): Flow<User>
}
