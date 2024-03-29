/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.rank

import com.fasterxml.jackson.annotation.JsonProperty

internal data class RankResponse(
    @field:JsonProperty("rankings")
    val rankings: List<RankingsItemResponse?>? = null,

    @field:JsonProperty("cursorUserId")
    val cursorUserId: Int? = null,

    @field:JsonProperty("cursorScore")
    val cursorScore: Int? = null,

    @field:JsonProperty("hasNext")
    val hasNext: Boolean? = null,
)
