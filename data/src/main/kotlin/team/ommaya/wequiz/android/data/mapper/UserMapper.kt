/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.mapper

import team.ommaya.wequiz.android.data.model.user.UserInformationResponse
import team.ommaya.wequiz.android.domain.model.user.UserInformation

internal fun UserInformationResponse.toDomain() =
    UserInformation(
        id = requireNotNull(id),
        nickname = requireNotNull(nickname),
        description = requireNotNull(description),
    )
