/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.verifycode

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentVerifyCodeBinding
import team.ommaya.wequiz.android.intro.IntroMode
import team.ommaya.wequiz.android.intro.IntroViewModel
import team.ommaya.wequiz.android.intro.IntroViewModel.Companion.INITIAL_VERIFY_TIME
import team.ommaya.wequiz.android.intro.VerifyCodeUiEvent
import team.ommaya.wequiz.android.utils.KeyboardVisibilityUtils
import team.ommaya.wequiz.android.utils.ProgressDialog
import team.ommaya.wequiz.android.utils.SnackbarMode
import team.ommaya.wequiz.android.utils.WeQuizDialog
import team.ommaya.wequiz.android.utils.WeQuizDialogContents
import team.ommaya.wequiz.android.utils.WeQuizSnackbar
import team.ommaya.wequiz.android.utils.isValidInputLength
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class VerifyCodeFragment :
    BaseViewBindingFragment<FragmentVerifyCodeBinding>(FragmentVerifyCodeBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()
    private var timer: Job = Job()
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startTimer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initKeyboardVisibilityUtils()
        collectFlow()
    }

    private fun startTimer() {
        if (timer.isActive) timer.cancel()
        introViewModel.setIsVerifyTimeOut(false)

        timer = lifecycleScope.launch {
            var remainTime = START_TIME

            while (remainTime != END_TIME) {
                delay(TIMER_INTERVAL)
                remainTime -= TIMER_INTERVAL
                introViewModel.setVerifyTime(formatMilliseconds(remainTime))
            }
            introViewModel.sendVerifyCodeEvent(VerifyCodeUiEvent.TIMEOUT)
            timer.cancel()
        }
    }

    private fun initView() {
        with(binding) {
            etVerifyCodeInput.addTextChangedListener {
                val text = etVerifyCodeInput.text.toString()

                if (isValidInputLength(text, VERIFY_CODE_LENGTH)) {
                    if (!introViewModel.isVerifyTimeOut.value) {
                        hideKeyboard()
                        introViewModel.verifyCode(text)
                        progressDialog.show(
                            requireActivity().supportFragmentManager,
                            "createProgress",
                        )
                        setTimerAndResendBtnVisibility(false)
                    } else {
                        showFailureWeQuizSnackbar(R.string.verify_code_resend_time_out)
                    }
                }
            }

            textInputLayoutVerifyCodeInput.setEndIconOnClickListener {
                startTimer()
                introViewModel.resendVerifyCode(requireActivity())
            }

            btnVerifyCodeBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    introViewModel.verifyTime.collect { remainTime ->
                        binding.tvVerifyCodeTimer.text = remainTime
                    }
                }

                launch {
                    introViewModel.verifyCodeEventFlow.collect { event ->
                        setTimerAndResendBtnVisibility(true)
                        progressDialog.dismiss()
                        when (event) {
                            VerifyCodeUiEvent.RESEND -> {
                                showSuccessWeQuizSnackbar(R.string.verify_code_resend)
                            }
                            VerifyCodeUiEvent.TIMEOUT -> {
                                showFailureWeQuizSnackbar(R.string.verify_code_time_out)
                                introViewModel.setIsVerifyTimeOut(true)
                            }
                            VerifyCodeUiEvent.REGISTERED -> {
                                findNavController().navigate(R.id.action_verifyCodeFragment_to_welcomeFragment)
                            }
                            VerifyCodeUiEvent.UNREGISTERED -> {
                                when (introViewModel.mode.value) {
                                    IntroMode.LOGIN -> {
                                        showWeQuizDialog(
                                            { findNavController().popBackStack() },
                                            { findNavController().navigate(R.id.action_verifyCodeFragment_to_joinFragment) },
                                        )
                                    }
                                    IntroMode.SIGNUP -> {
                                        findNavController().navigate(R.id.action_verifyCodeFragment_to_joinFragment)
                                    }
                                }
                            }
                            VerifyCodeUiEvent.FAILURE -> {
                                showFailureWeQuizSnackbar(R.string.verify_code_incorrect)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val format = SimpleDateFormat("m:ss", Locale.KOREA)
        return format.format(Date(milliseconds))
    }

    private fun showSuccessWeQuizSnackbar(messageId: Int) {
        WeQuizSnackbar.make(
            binding.root,
            getString(messageId),
        ).show()
    }

    private fun showFailureWeQuizSnackbar(messageId: Int) {
        WeQuizSnackbar.make(
            binding.root,
            getString(messageId),
            SnackbarMode.FAILURE,
        ).show()
    }

    private fun showWeQuizDialog(negativeAction: () -> Unit, positiveAction: () -> Unit) {
        val weQuizDialogContents = WeQuizDialogContents(
            title = getString(R.string.sign_up_dialog),
            negativeBtnText = getString(R.string.negative),
            positiveBtnText = getString(R.string.sign_up),
            negativeBtnAction = { negativeAction() },
            positiveBtnAction = { positiveAction() },
        )

        WeQuizDialog(weQuizDialogContents).show(childFragmentManager, "tag")
    }

    private fun setTimerAndResendBtnVisibility(isVisible: Boolean) {
        if (isVisible) {
            with(binding) {
                tvVerifyCodeTimer.visibility = View.VISIBLE
                textInputLayoutVerifyCodeInput.isEndIconVisible = true
            }
        } else {
            with(binding) {
                tvVerifyCodeTimer.visibility = View.INVISIBLE
                textInputLayoutVerifyCodeInput.isEndIconVisible = false
            }
        }
    }

    private fun initKeyboardVisibilityUtils() {
        keyboardVisibilityUtils = KeyboardVisibilityUtils(
            window = requireActivity().window,
            onHideKeyboard = {
                binding.etVerifyCodeInput.clearFocus()
            },
        )
    }

    private fun hideKeyboard() {
        with(binding) {
            WindowInsetsControllerCompat(
                requireActivity().window,
                etVerifyCodeInput,
            ).hide(WindowInsetsCompat.Type.ime())

            etVerifyCodeInput.clearFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        introViewModel.setIsVerifyTimeOut(true)
        introViewModel.setVerifyTime(INITIAL_VERIFY_TIME)
        keyboardVisibilityUtils.detachKeyboardListeners()
        timer.cancel()
    }

    companion object {
        const val VERIFY_CODE_LENGTH = 6
        const val TIMER_INTERVAL = 100L
        const val START_TIME = 120_000L
        const val END_TIME = 0L
    }
}
