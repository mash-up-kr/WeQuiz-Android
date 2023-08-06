/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.repository

import androidx.datastore.core.DataStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.first
import team.ommaya.wequiz.android.data.mapper.toDomain
import team.ommaya.wequiz.android.data.model.user.SignUpRequest
import team.ommaya.wequiz.android.data.model.user.SignUpResponse
import team.ommaya.wequiz.android.data.model.user.UserInformationFormattedResponse
import team.ommaya.wequiz.android.data.preference.UserPreference
import team.ommaya.wequiz.android.domain.model.user.UserInformation
import team.ommaya.wequiz.android.domain.repository.UserRepository
import javax.inject.Inject

class UnregisteredException : Exception()

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: DataStore<UserPreference>,
    private val client: HttpClient,
) : UserRepository {
    override suspend fun getUser() = userDataStore.data.first().toUserModel()

    override suspend fun getInformation(token: String): UserInformation {
        val response =
            client
                .get("user") {
                    header("x-wequiz-token", token)
                }
                .body<UserInformationFormattedResponse>()
        when (response.code) {
            "SUCCESS" -> {
                return response.toDomain()
            }
            "WEC401" -> {
                throw UnregisteredException()
            }
            else -> {
                throw Exception(response.code)
            }
        }
    }

    override suspend fun postUserJoin(
        token: String,
        phone: String,
        nickname: String,
        description: String,
    ): String {
        val response =
            client
                .post("user/join") {
                    header("x-wequiz-token", token)
                    setBody(
                        SignUpRequest(
                            token = token,
                            phone = phone,
                            nickname = nickname,
                            description = description,
                        ),
                    )
                }
                .body<SignUpResponse>()
        return response.code
    }

    override suspend fun saveUserToken(token: String) {
        userDataStore.updateData { preference ->
            preference.copy(isLogin = true, token = token)
        }
    }

    override suspend fun logout() {
        userDataStore.updateData { preference ->
            preference.copy(isLogin = false, token = "")
        }
    }
}
