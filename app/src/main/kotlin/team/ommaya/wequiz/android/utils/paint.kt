/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor

@Stable
fun Modifier.fitPaint(
    @DrawableRes drawableId: Int,
    tint: WeQuizColor = WeQuizColor.Unspecified,
) =
    composed {
        paint(
            painter = painterResource(drawableId),
            colorFilter = tint.toRememberColorFilterOrNull(),
            contentScale = ContentScale.Fit,
        )
    }