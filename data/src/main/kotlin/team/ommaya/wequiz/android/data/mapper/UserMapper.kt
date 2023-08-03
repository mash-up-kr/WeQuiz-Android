/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.mapper

import team.ommaya.wequiz.android.data.model.user.UserResponse
import team.ommaya.wequiz.android.domain.model.user.UserData
import team.ommaya.wequiz.android.domain.model.user.UserInformation

internal fun UserResponse.toDomain() =
    UserInformation(
        code = requireNotNull(code),
        data = UserData(
            requireNotNull(data?.id),
            requireNotNull(data?.nickname),
            requireNotNull(data?.description),
        ),
        message = requireNotNull(message),
    )
