/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:OptIn(ExperimentalContracts::class)

package team.ommaya.wequiz.android.domain

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

internal inline fun <T> runSuspendCatching(block: () -> T): Result<T> {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return runCatching(block).also { result ->
        val maybeException = result.exceptionOrNull()
        if (maybeException is CancellationException) throw maybeException
    }
}
