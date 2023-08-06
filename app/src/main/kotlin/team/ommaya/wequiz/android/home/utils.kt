/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home

import android.app.Activity
import team.ommaya.wequiz.android.intro.IntroActivity

fun Activity.obtainToken() =
    intent.getStringExtra(IntroActivity.TOKEN) ?: error("Missing token.")
