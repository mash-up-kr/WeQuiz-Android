/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import android.content.Context
import team.ommaya.wequiz.android.R

fun getResultContext(context: Context, score: Int): String {
    return when (score) {
        in 30..49 -> context.getString(R.string.result_title2)
        in 50..69 -> context.getString(R.string.result_title3)
        in 70..89 -> context.getString(R.string.result_title4)
        in 90..100 -> context.getString(R.string.result_title5)
        else -> context.getString(R.string.result_title1)
    }
}

fun getResultImage(score: Int): Int {
    return when (score) {
        in 30..49 -> team.ommaya.wequiz.android.design.resource.R.drawable.img_result_02
        in 50..69 -> team.ommaya.wequiz.android.design.resource.R.drawable.img_result_03
        in 70..89 -> team.ommaya.wequiz.android.design.resource.R.drawable.img_result_04
        in 90..100 -> team.ommaya.wequiz.android.design.resource.R.drawable.img_result_05
        else -> team.ommaya.wequiz.android.design.resource.R.drawable.img_result_01
    }
}
