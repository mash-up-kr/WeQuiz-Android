/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package team.ommaya.wequiz.android.design.resource.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@JvmInline
@Immutable
public value class WeQuizColor private constructor(public val value: Color) :
    ReadOnlyProperty<Any, Color> {

    @Stable
    public fun toBrush(): SolidColor = SolidColor(value)

    @Stable
    public fun toColorFilterOrNull(): ColorFilter? =
        if (this == Unspecified) null else ColorFilter.tint(value)

    @Composable
    public fun toRememberColorFilterOrNull(): ColorFilter? = remember(::toColorFilterOrNull)

    @Stable
    public fun change(alpha: Float): WeQuizColor =
        if (alpha == value.alpha) this
        else WeQuizColor(value.copy(alpha = alpha))

    public companion object {
        @Stable
        public val Unspecified: WeQuizColor = WeQuizColor(Color.Unspecified)

        @Stable
        public val P1: WeQuizColor = WeQuizColor(Color(0xFF9146FF))

        @Stable
        public val S1: WeQuizColor = WeQuizColor(Color(0xFF6AF2BB))

        @Stable
        public val S2: WeQuizColor = WeQuizColor(Color(0xFF72D0C9))

        @Stable
        public val S3: WeQuizColor = WeQuizColor(Color(0xFF7AAED7))

        @Stable
        public val S4: WeQuizColor = WeQuizColor(Color(0xFF828BE4))

        @Stable
        public val S5: WeQuizColor = WeQuizColor(Color(0xFF8A69F2))

        @Stable
        public val G1: WeQuizColor = WeQuizColor(Color(0xFFF1F3F5))

        @Stable
        public val G2: WeQuizColor = WeQuizColor(Color(0xFFE9ECEF))

        @Stable
        public val G3: WeQuizColor = WeQuizColor(Color(0xFFDEE2E6))

        @Stable
        public val G4: WeQuizColor = WeQuizColor(Color(0xFFADB5BD))

        @Stable
        public val G5: WeQuizColor = WeQuizColor(Color(0xFF6E7277))

        @Stable
        public val G6: WeQuizColor = WeQuizColor(Color(0xFF4B4C4E))

        @Stable
        public val G7: WeQuizColor = WeQuizColor(Color(0xFF353638))

        @Stable
        public val G8: WeQuizColor = WeQuizColor(Color(0xFF1B1B1B))

        @Stable
        public val G9: WeQuizColor = WeQuizColor(Color(0xFF121212))

        @Stable
        public val Disabled: WeQuizColor = WeQuizColor(Color(0xFF542F89))

        @Stable
        public val Alert: WeQuizColor = WeQuizColor(Color(0xFFFF5D47))

        @Stable
        public val Dimmed: WeQuizColor = WeQuizColor(Color(0xB2121212))

        @Stable
        public val White: WeQuizColor = WeQuizColor(Color(0xFFFFFFFF))

        @Stable
        public val Black: WeQuizColor = WeQuizColor(Color(0xE5161616))
    }

    @Stable
    override fun getValue(thisRef: Any, property: KProperty<*>): Color = value
}
