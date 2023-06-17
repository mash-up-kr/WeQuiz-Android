/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("unused")

package team.ommaya.wequiz.android.design.resource.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
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
class WeQuizTypography private constructor(
    val color: WeQuizColor = WeQuizColor.Black,
    val size: TextUnit,
    val weight: FontWeight,
    val letterSpacing: TextUnit,
    val lineHeight: TextUnit,
    val textAlign: TextAlign = TextAlign.Left,
    val fontFamily: FontFamily = pretendard,
) {
    @Stable
    fun asComposeStyle() =
        TextStyle(
            color = color.value,
            fontSize = size,
            fontFamily = fontFamily,
            fontWeight = weight,
            letterSpacing = letterSpacing,
            textAlign = textAlign,
            lineHeight = lineHeight,
        )

    @Stable
    fun change(
        color: WeQuizColor = this.color,
        textAlign: TextAlign = this.textAlign,
    ) =
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

    override fun toString() =
        "WeQuizTypography(" +
            "color=$color, " +
            "size=$size, " +
            "weight=$weight, " +
            "letterSpacing=$letterSpacing, " +
            "lineHeight=$lineHeight, " +
            "textAlign=$textAlign, " +
            "fontFamily=$fontFamily" +
            ")"

    companion object {
        @Stable
        val B48 = WeQuizTypography(
            size = 48.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 54.sp,
        )

        @Stable
        val B34 = WeQuizTypography(
            size = 34.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 46.sp,
        )

        @Stable
        val M32 = WeQuizTypography(
            size = 32.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 38.sp,
        )

        @Stable
        val B24 = WeQuizTypography(
            size = 24.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 34.sp,
        )

        @Stable
        val M24 = WeQuizTypography(
            size = 24.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 34.sp,
        )

        @Stable
        val R24 = WeQuizTypography(
            size = 24.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 34.sp,
        )

        @Stable
        val B20 = WeQuizTypography(
            size = 20.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        val M20 = WeQuizTypography(
            size = 20.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        val R20 = WeQuizTypography(
            size = 20.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        val B18 = WeQuizTypography(
            size = 18.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 26.sp,
        )

        @Stable
        val B16 = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        val SB16 = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.SemiBold,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        val M16 = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        val R16 = WeQuizTypography(
            size = 16.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )

        @Stable
        val B14 = WeQuizTypography(
            size = 14.sp,
            weight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            lineHeight = 20.sp,
        )

        @Stable
        val M14 = WeQuizTypography(
            size = 14.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 20.sp,
        )

        @Stable
        val R14 = WeQuizTypography(
            size = 14.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 20.sp,
        )

        @Stable
        val M12 = WeQuizTypography(
            size = 12.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 18.sp,
        )

        @Stable
        val R12 = WeQuizTypography(
            size = 12.sp,
            weight = FontWeight.Regular,
            letterSpacing = (-0.3).sp,
            lineHeight = 18.sp,
        )

        @Stable
        val M10 = WeQuizTypography(
            size = 10.sp,
            weight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 14.sp,
        )
    }
}

val FontWeight.Companion.Regular inline get() = Normal
