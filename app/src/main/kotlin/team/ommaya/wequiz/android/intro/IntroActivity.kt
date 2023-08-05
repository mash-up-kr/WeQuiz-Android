package team.ommaya.wequiz.android.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityIntroBinding
import team.ommaya.wequiz.android.home.main.HomeMainActivity

@AndroidEntryPoint
class IntroActivity : BaseViewBindingActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {
    private val introViewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkIsLogin()
        startHomeMainActivity()
    }

    private fun checkIsLogin() {
        introViewModel.checkIsLogin()
    }

    private fun startHomeMainActivity() {
        lifecycleScope.launch {
            introViewModel.isLogin.collect { isLogin ->
                if (isLogin) {
                    val intent = Intent(this@IntroActivity, HomeMainActivity::class.java)
                    intent.putExtra(TOKEN, introViewModel.token.value)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    companion object {
        const val TOKEN = "token"
    }
}
