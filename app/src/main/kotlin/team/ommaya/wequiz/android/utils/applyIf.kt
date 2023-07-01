/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("unused")
@file:OptIn(ExperimentalContracts::class)

package team.ommaya.wequiz.android.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun <T> T.applyIf(condition: Boolean, run: T.() -> T): T {
    contract { callsInPlace(run, InvocationKind.EXACTLY_ONCE) }
    return if (condition) run() else this
}

inline fun <T> T.applyIf(conditionBuilder: (receiver: T) -> Boolean, run: T.() -> T): T {
    contract {
        callsInPlace(conditionBuilder, InvocationKind.EXACTLY_ONCE)
        callsInPlace(run, InvocationKind.EXACTLY_ONCE)
    }
    return if (conditionBuilder(this)) run() else this
}
