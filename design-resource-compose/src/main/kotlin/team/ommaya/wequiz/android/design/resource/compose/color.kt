/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package team.ommaya.wequiz.android.design.resource.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@JvmInline
@Immutable
value class WeQuizColor private constructor(val value: Color) : ReadOnlyProperty<Any, Color> {
    @Stable
    fun toBrush() = SolidColor(value)

    @Stable
    fun toColorFilterOrNull() =
        if (this == Unspecified) null else ColorFilter.tint(value)

    @Stable
    fun change(alpha: Float) =
        if (alpha == value.alpha) this
        else WeQuizColor(value.copy(alpha = alpha))

    companion object {
        @Stable
        val Unspecified = WeQuizColor(Color.Unspecified)

        @Stable
        val P1 = WeQuizColor(Color(0xFF9146FF))

        @Stable
        val S1 = WeQuizColor(Color(0xFF6AF2BB))

        @Stable
        val S2 = WeQuizColor(Color(0xFF72D0C9))

        @Stable
        val S3 = WeQuizColor(Color(0xFF7AAED7))

        @Stable
        val S4 = WeQuizColor(Color(0xFF828BE4))

        @Stable
        val S5 = WeQuizColor(Color(0xFF8A69F2))

        @Stable
        val G1 = WeQuizColor(Color(0xFFF1F3F5))

        @Stable
        val G2 = WeQuizColor(Color(0xFFE9ECEF))

        @Stable
        val G3 = WeQuizColor(Color(0xFFDEE2E6))

        @Stable
        val G4 = WeQuizColor(Color(0xFFADB5BD))

        @Stable
        val G5 = WeQuizColor(Color(0xFF6E7277))

        @Stable
        val G6 = WeQuizColor(Color(0xFF4B4C4E))

        @Stable
        val G7 = WeQuizColor(Color(0xFF353638))

        @Stable
        val G8 = WeQuizColor(Color(0xFF1B1B1B))

        @Stable
        val G9 = WeQuizColor(Color(0xFF121212))

        @Stable
        val Disabled = WeQuizColor(Color(0xFF542F89))

        @Stable
        val Alert = WeQuizColor(Color(0xFFFF5D47))

        @Stable
        val Dimmed = WeQuizColor(Color(0xB2121212))

        @Stable
        val Black = WeQuizColor(Color(0xE5161616))
    }

    @Stable
    override fun getValue(thisRef: Any, property: KProperty<*>) = value
}
