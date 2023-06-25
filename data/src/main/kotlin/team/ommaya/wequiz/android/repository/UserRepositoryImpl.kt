/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import team.ommaya.wequiz.android.domain.model.User
import team.ommaya.wequiz.android.domain.repository.UserRepository
import team.ommaya.wequiz.android.preference.UserPreference
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: DataStore<UserPreference>
) : UserRepository {
    override fun getUser(): Flow<User> = userDataStore.data.map {
        it.toUserModel()
    }
}