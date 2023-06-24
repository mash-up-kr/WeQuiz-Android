/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

@Suppress("NOTHING_TO_INLINE")
inline fun PluginDependenciesSpec.wequiz(pluginName: String): PluginDependencySpec =
    id("wequiz.plugin.$pluginName")
