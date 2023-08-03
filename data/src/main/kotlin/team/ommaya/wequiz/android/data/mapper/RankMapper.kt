/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.mapper

import team.ommaya.wequiz.android.data.model.rank.RankResponse
import team.ommaya.wequiz.android.domain.model.rank.Rank
import team.ommaya.wequiz.android.domain.model.rank.RankingsItem
import team.ommaya.wequiz.android.domain.model.rank.UserSimpleInformation

internal fun RankResponse.toDomain() =
    Rank(
        cursorUserId = cursorUserId,
        cursorScore = cursorScore,
        rankings = requireNotNull(rankings).map { item ->
            requireNotNull(item)
            RankingsItem(
                userInfo = UserSimpleInformation(
                    id = requireNotNull(item.userInfo?.id),
                    name = requireNotNull(item.userInfo?.name),
                ),
                score = requireNotNull(item.score),
            )
        },
        hasNext = requireNotNull(hasNext),
    )
