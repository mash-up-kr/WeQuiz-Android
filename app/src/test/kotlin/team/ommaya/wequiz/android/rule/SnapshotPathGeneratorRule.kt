/*
 * Designed and developed by Duckie Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/quack-quack-android/blob/main/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE")

package team.ommaya.wequiz.android.rule

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File

const val BaseSnapshotPath = "src/test/snapshots"

class SnapshotPathGeneratorRule(private val domain: String) : TestWatcher() {
    init {
        File("$BaseSnapshotPath/$domain").mkdirs()
    }

    private var realtimeTestMethodName: String? = null

    override fun starting(description: Description) {
        realtimeTestMethodName = description.methodName
    }

    operator fun invoke(isGif: Boolean = false) =
        File("$BaseSnapshotPath/$domain/$realtimeTestMethodName.${if (isGif) "gif" else "png"}")
}