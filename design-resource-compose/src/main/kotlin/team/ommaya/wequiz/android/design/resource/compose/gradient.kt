/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("unused")

package team.ommaya.wequiz.android.design.resource.compose

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

public object WeQuizGradient {
    @Stable
    public val Black: Brush = Brush.linearGradient(
        colorStops = arrayOf(
            0f to Color(0xFF121212),
            0.53f to Color(0xFF121212),
            0.72f to Color(0xCC121212),
            1f to Color(0x00121212),
        ),
        // CW270
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset.Zero,
    )

    @Stable
    public val Secondary: Brush = Brush.linearGradient(
        colorStops = arrayOf(
            0f to Color(0xFFCDF9FF),
            0.41f to Color(0xFF6EEAF2),
            1f to Color(0xFF6AF2BB),
        ),
        // CW315
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f),
    )
}
