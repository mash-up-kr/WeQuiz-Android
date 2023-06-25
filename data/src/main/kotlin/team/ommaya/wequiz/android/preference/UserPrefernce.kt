/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.preference

import kotlinx.serialization.Serializable

@Serializable
data class UserPreference(
    val isLogin: Boolean,
    val isFirstAccess: Boolean,
    val isSaveTemporarily: Boolean,
){
    companion object{
        const val localPath = "user_preferences"
    }
}
