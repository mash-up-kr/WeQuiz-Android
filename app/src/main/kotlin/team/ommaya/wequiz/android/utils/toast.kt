/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

internal fun Activity.toast(message: String) =
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).also(Toast::show)

internal fun Fragment.toast(message: String) =
    requireActivity().toast(message)
