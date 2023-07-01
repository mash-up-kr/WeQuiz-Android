/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.snap

object AnimationSpecTestingSupports {
    var isTestMode = false

    operator fun <T> invoke(animationSpec: DurationBasedAnimationSpec<T>) =
        if (isTestMode) snap() else animationSpec
}
