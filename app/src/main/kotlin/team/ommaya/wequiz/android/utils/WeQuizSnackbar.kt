/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import team.ommaya.wequiz.android.databinding.SnackbarWequizBinding

class WeQuizSnackbar(
    view: View,
    private val message: String,
    private val snackbarMode: SnackbarMode,
) {
    companion object {
        fun make(view: View, message: String, snackbarMode: SnackbarMode) =
            WeQuizSnackbar(view, message, snackbarMode)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 3000)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    private val inflater = LayoutInflater.from(context)
    private val binding = SnackbarWequizBinding.inflate(inflater)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(dpToPx(20), 0, dpToPx(20), dpToPx(50))
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }
    }

    private fun initData() {
        with(binding) {
            tvSnackbarMessage.text = message

            ivSnackbarIcon.background = ContextCompat.getDrawable(
                context,
                when (snackbarMode) {
                    SnackbarMode.Success -> team.ommaya.wequiz.android.design.resource.R.drawable.ic_circle_success
                    SnackbarMode.Error -> team.ommaya.wequiz.android.design.resource.R.drawable.ic_circle_error
                }
            )
        }
    }

    fun show() {
        snackbar.show()
    }

    private fun dpToPx(dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}

enum class SnackbarMode {
    Success, Error
}
