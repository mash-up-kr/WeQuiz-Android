/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.widget.TextView
import androidx.core.content.ContextCompat
import team.ommaya.wequiz.android.design.resource.R

fun setTextGradient(textView: TextView) {
    with(textView) {
        paint.shader = LinearGradient(
            paint.measureText(text.toString()) * 0.15f,
            textSize * 0.15f,
            paint.measureText(text.toString()) * 0.85f,
            textSize * 0.85f,
            intArrayOf(
                ContextCompat.getColor(
                    context,
                    R.color.S1_G_start,
                ),
                ContextCompat.getColor(
                    context,
                    R.color.S1_G_center,
                ),
                ContextCompat.getColor(
                    context,
                    R.color.S1_G_end,
                ),
            ),
            null,
            Shader.TileMode.CLAMP,
        )

        val matrix = Matrix()
        matrix.setRotate(
            275f,
            paint.measureText(text.toString()) / 2,
            textSize / 2,
        )
        paint.shader.setLocalMatrix(matrix)
    }
}
