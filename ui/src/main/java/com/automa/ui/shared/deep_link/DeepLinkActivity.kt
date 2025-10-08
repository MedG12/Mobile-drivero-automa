package com.automa.ui.shared.deep_link

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import com.automa.datastore.pref.Preferences
import com.automa.datastore.pref.PreferencesConstant
import com.automa.datastore.user_data.UserDataModel
import com.automa.datastore.user_data.UserRoleType
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivityDeepLinkBinding
import com.automa.ui.driver.home.HomeActivity
import com.automa.ui.mechanic.home.HomeMechanicActivity
import com.automa.ui.shared.auth.LoginActivity
import com.automa.ui.utils.isLoggedIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class DeepLinkActivity : BaseActivity() {
    companion object {
        const val DIRECTION_NOTIF = "notif"
        const val DIRECTION_ACCOUNT = "account"
        const val DIRECTION_TASK = "task"

        const val TYPE_NOTIF = "NOTIFICATION"
    }
    private lateinit var binding: ActivityDeepLinkBinding
    @Inject lateinit var userData: DataStore<UserDataModel>
    @Inject lateinit var pref: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeepLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        processDeepLink()
    }

    private fun processDeepLink() {
        val data = intent?.data
        data?.let {
            val path = it.path
            Log.d("TAG-Deeplink", "processDeepLink: path: $path, segments: ${it.pathSegments}")
            it.pathSegments?.let { segments ->
                when (segments.first()) {
                    "notif" -> {
                        pref.saveString(PreferencesConstant.PENDING_DEEPLINK_TYPE, TYPE_NOTIF)
                        pref.saveString(PreferencesConstant.PENDING_DEEPLINK_VALUE, path ?: "")
                        goToHome()
                    }
                }
            }
        }
    }

    private fun goToHome() {
        runBlocking {
            if (isLoggedIn(userData.data.first())) {
                when(userData.data.first().roleType) {
                    UserRoleType.DRIVER -> {
                        startActivity(Intent(this@DeepLinkActivity, HomeActivity::class.java))
                        finish()
                    }
                    UserRoleType.HEAD_MECHANIC, UserRoleType.MECHANIC -> {
                        startActivity(Intent(this@DeepLinkActivity, HomeMechanicActivity::class.java))
                        finish()
                    }
                    else -> {
                        goToLogin()
                    }
                }
            } else {
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}