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
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import team.ommaya.wequiz.android.databinding.DialogWeQuizBinding

class WeQuizDialog(
    private val weQuizDialogContents: WeQuizDialogContents,
) : DialogFragment() {
    private var _binding: DialogWeQuizBinding? = null

    private val binding: DialogWeQuizBinding
        get() = _binding ?: error("Binding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogWeQuizBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            tvWeQuizDialogTitle.text = weQuizDialogContents.title
            btnWeQuizDialogNegative.text = weQuizDialogContents.negativeBtnText
            btnWeQuizDialogPositive.text = weQuizDialogContents.positiveBtnText

            btnWeQuizDialogNegative.setOnClickListener {
                weQuizDialogContents.negativeBtnAction()
                dismiss()
            }

            btnWeQuizDialogPositive.setOnClickListener {
                weQuizDialogContents.positiveBtnAction()
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class WeQuizDialogContents(
    val title: String,
    val negativeBtnText: String,
    val positiveBtnText: String,
    val negativeBtnAction: () -> Unit,
    val positiveBtnAction: () -> Unit,
)
