/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.util.fastFirstOrNull

@Stable
operator fun List<Measurable>.get(layoutId: String) =
    fastFirstOrNull { measurable ->
        measurable.layoutId == layoutId
    } ?: error("주어진 layoutId($layoutId)에 해당하는 Measurable을 찾을 수 없습니다.")
