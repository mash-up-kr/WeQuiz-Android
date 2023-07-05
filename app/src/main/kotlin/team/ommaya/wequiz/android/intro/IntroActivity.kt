package team.ommaya.wequiz.android.intro

import android.os.Bundle
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityIntroBinding

class IntroActivity : BaseViewBindingActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        const val LOG_IN_MODE = 0
        const val SIGN_UP_MODE = 1
    }
}
