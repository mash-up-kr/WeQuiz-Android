/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

fun isValidInputLength(text: String, length: Int) = text.length == length
fun isValidInputLengthRange(text: String, min: Int, max: Int) = text.length in min..max
