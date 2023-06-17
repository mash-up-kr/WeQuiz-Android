/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("unused")

package team.ommaya.wequiz.android.design.resource.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val pretendard = FontFamily(
    Font(resId = R.font.drc_pretendard_bold, weight = FontWeight.Bold),
    Font(resId = R.font.drc_pretendard_semi_bold, weight = FontWeight.SemiBold),
    Font(resId = R.font.drc_pretendard_medium, weight = FontWeight.Medium),
    Font(resId = R.font.drc_pretendard_regular, weight = FontWeight.Regular),
)

@Immutable
public class WeQuizTypography private constructor(
    public val color: WeQuizColor = WeQuizColor.Black,
    public val size: TextUnit,
    public val weight: FontWeight,
    public val letterSpacing: TextUnit,
    public val lineHeight: TextUnit,
    public val textAlign: TextAlign = TextAlign.Left,
    public val fontFamily: FontFamily = pretendard,
) {
    @Stable
    public fun asComposeStyle(): TextStyle =
        TextStyle(
            color = color.value,
            fontSize = size,
            fontFamily = fontFamily,
            fontWeight = weight,
            letterSpacing = letterSpacing,
            textAlign = textAlign,
            lineHeight = lineHeight,
        )

    @Composable
    public fun asRememberComposeStyle(): TextStyle = remember(::asComposeStyle)

    @Stable
    public fun change(
        color: WeQuizColor = this.color,
        textAlign: TextAlign = this.textAlign,
    ): WeQuizTypography =
        if (color == this.color && textAlign == this.textAlign) {
            this
        } else {
            WeQuizTypography(
                color = color,
                size = size,
                weight = weight,
                letterSpacing = letterSpacing,
                lineHeight = lineHeight,
                textAlign = textAlign,
            )
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WeQuizTypography) return false

        if (color != other.color) return false
        if (size != other.size) return false
        if (weight != other.weight) return false
        if (letterSpacing != other.letterSpacing) return false
        if (lineHeight != other.lineHeight) return false
        if (textAlign != other.textAlign) return false
        if (fontFamily != other.fontFamily) return false

        return true
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + letterSpacing.hashCode()
        result = 31 * result + lineHeight.hashCode()
        result = 31 * result + textAlign.hashCode()
        result = 31 * result + fontFamily.hashCode()
        return result
    }

    override fun toString(): String =
        "WeQuizTypography(" +
                "color=$color, " +
                "size=$size, " +
                "weight=$weight, " +
                "letterSpacing=$letterSpacing, " +
                "lineHeight=$lineHeight, " +
                "textAlign=$textAlign, " +
                "fontFamily=$fontFamily" +
                ")"

    public companion object {
        @Stable
        public val B48: WeQuizTypography = WeQuizTypography(
            size = 48.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 54.sp,
        )

        @Stable
        public val B34: WeQuizTypography = WeQuizTypography(
            size = 34.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 46.sp,
        )

        @Stable
        public val M32: WeQuizTypography = WeQuizTypography(
            size = 32.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 38.sp,
        )

        @Stable
        public val B24: WeQuizTypography = WeQuizTypography(
            size = 24.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 34.sp,
        )

        @Stable
        public val M24: WeQuizTypography = WeQuizTypography(
            size = 24.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 34.sp,
        )

        @Stable
        public val R24: WeQuizTypography = WeQuizTypography(
            size = 24.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 34.sp,
        )

        @Stable
        public val B20: WeQuizTypography = WeQuizTypography(
            size = 20.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        public val M20: WeQuizTypography = WeQuizTypography(
            size = 20.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        public val R20: WeQuizTypography = WeQuizTypography(
            size = 20.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        public val B18: WeQuizTypography = WeQuizTypography(
            size = 18.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        public val M18: WeQuizTypography = WeQuizTypography(
            size = 18.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        public val B16: WeQuizTypography = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        public val SB16: WeQuizTypography = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.SemiBold,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        public val M16: WeQuizTypography = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        public val R16: WeQuizTypography = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        public val B14: WeQuizTypography = WeQuizTypography(
            size = 14.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 20.sp,
        )

        @Stable
        public val M14: WeQuizTypography = WeQuizTypography(
            size = 14.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 20.sp,
        )

        @Stable
        public val R14: WeQuizTypography = WeQuizTypography(
            size = 14.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 20.sp,
        )

        @Stable
        public val M12: WeQuizTypography = WeQuizTypography(
            size = 12.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 18.sp,
        )

        @Stable
        public val R12: WeQuizTypography = WeQuizTypography(
            size = 12.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 18.sp,
        )

        @Stable
        public val M10: WeQuizTypography = WeQuizTypography(
            size = 10.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 14.sp,
        )

        @Stable
        public val R10: WeQuizTypography = WeQuizTypography(
            size = 10.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 14.sp,
        )
    }
}

public val FontWeight.Companion.Regular: FontWeight inline get() = Normal
