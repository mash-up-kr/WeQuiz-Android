/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import team.ommaya.wequiz.android.databinding.DialogProgressBinding

class ProgressDialog : DialogFragment() {
    private var _binding: DialogProgressBinding? = null
    private val binding
        get() = _binding ?: error("Binding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogProgressBinding.inflate(inflater)

        dialog?.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.requestFeature(Window.FEATURE_NO_TITLE)
        }
        dialog?.let {
            isCancelable = false
        }
        return binding.root
    }
}
