/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Constraints

@Stable
fun Constraints.asLoose(
    width: Boolean = false,
    height: Boolean = false,
): Constraints =
    copy(
        minWidth = if (width) 0 else minWidth,
        minHeight = if (height) 0 else minHeight,
    )
