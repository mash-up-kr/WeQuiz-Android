/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.verifycode

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentVerifyCodeBinding
import team.ommaya.wequiz.android.intro.IntroViewModel
import team.ommaya.wequiz.android.utils.SnackbarMode
import team.ommaya.wequiz.android.utils.WeQuizSnackbar
import team.ommaya.wequiz.android.utils.isValidInputLength
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VerifyCodeFragment :
    BaseViewBindingFragment<FragmentVerifyCodeBinding>(FragmentVerifyCodeBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()

    private val timer: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startTime()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        collectFlow()
    }

    private fun startTime() {
        if (timer.isActive) timer.cancel()

        lifecycleScope.launch {
            var currentTimer = START_TIME

            while (currentTimer != END_TIME) {
                delay(TIMER_INTERVAL)
                currentTimer -= TIMER_INTERVAL
                introViewModel.setVerifyTime(formatMilliseconds(currentTimer))
                introViewModel.isVerifyTimeOut
            }

            WeQuizSnackbar.make(binding.root, "인증번호를 재전송했어요", SnackbarMode.Failure).show()
            timer.cancel()
        }
    }

    private fun initView() {
        with(binding) {
            with(etVerifyCodeInput) {
                addTextChangedListener {
                    val text = text.toString()

                    if (isValidInputLength(text, VERIFY_CODE_LENGTH)) {
                        if (text == TEST_VERIFY_CODE) {
                            onVerificationSucceed()
                        } else {
                            // 인증번호가 올바르지 않아요 snack bar trigger
                        }
                    }
                }
            }
        }
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                introViewModel.verifyTime.collect { time ->
                    binding.tvVerifyCodeTimer.text = time
                }
            }
        }
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val format = SimpleDateFormat("m:ss", Locale.KOREA)
        return format.format(Date(milliseconds))
    }

    private fun onVerificationSucceed() {
        introViewModel.setVerificationSucceed(true)
        findNavController().popBackStack()
    }

    companion object {
        const val VERIFY_CODE_LENGTH = 6
        const val TIMER_INTERVAL = 100L
        const val START_TIME = 18_000L
        const val END_TIME = 0L
        const val TEST_VERIFY_CODE = "123456"
    }
}
