package team.ommaya.wequiz.android.intro

import android.os.Bundle
import androidx.activity.viewModels
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityIntroBinding

class IntroActivity : BaseViewBindingActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {
    private val introViewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
