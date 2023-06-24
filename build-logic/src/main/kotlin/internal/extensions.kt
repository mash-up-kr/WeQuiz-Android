/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE")

package internal

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.NamedDomainObjectContainerScope
import org.gradle.kotlin.dsl.getByType

internal val Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.applyPlugins(vararg plugins: String) {
    plugins.forEach(pluginManager::apply)
}

internal inline operator fun <T : Any, C : NamedDomainObjectContainer<T>> C.invoke(
    configuration: Action<NamedDomainObjectContainerScope<T>>,
): C = apply { configuration.execute(NamedDomainObjectContainerScope.of(this)) }
