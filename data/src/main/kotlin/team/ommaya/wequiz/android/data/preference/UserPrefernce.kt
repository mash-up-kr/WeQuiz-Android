/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.preference

import kotlinx.serialization.Serializable
import team.ommaya.wequiz.android.domain.model.user.User

@Serializable
data class UserPreference(
    val isLogin: Boolean,
    val isFirstAccess: Boolean,
    val isSaveTemporarily: Boolean,
) {
    fun toUserModel() = User(
        isLogin = isLogin,
        isFirstAccess = isFirstAccess,
        isSaveTemporarily = isSaveTemporarily,
    )

    companion object {
        const val localPath = "user_preferences"
    }
}
