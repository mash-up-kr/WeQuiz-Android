/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.preference

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferenceSerializer @Inject constructor() : Serializer<UserPreference> {
    override val defaultValue = UserPreference(
        isLogin = false,
        isFirstAccess = false,
        isSaveTemporarily = false,
        token = "",
    )

    override suspend fun readFrom(input: InputStream): UserPreference =
        try {
            Json.decodeFromString(
                UserPreference.serializer(),
                input.readBytes().decodeToString(),
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Fail to read UserPreference", exception)
        }

    override suspend fun writeTo(t: UserPreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(UserPreference.serializer(), t).encodeToByteArray(),
            )
        }
    }
}
