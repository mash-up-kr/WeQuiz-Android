package team.ommaya.wequiz.android.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
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

        initFirebasePlayIntegrity()
        checkIsLogin()
        startHomeMainActivity()
    }

    private fun initFirebasePlayIntegrity() {
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
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
