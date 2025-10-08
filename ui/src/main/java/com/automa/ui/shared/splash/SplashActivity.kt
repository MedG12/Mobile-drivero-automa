package com.automa.ui.shared.splash

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import com.automa.datastore.user_data.UserDataModel
import com.automa.datastore.user_data.UserRoleType
import com.automa.ui.shared.auth.LoginActivity
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivitySplashBinding
import com.automa.ui.driver.home.HomeActivity
import com.automa.ui.live_monitoring.LiveMonitoringActivity
import com.automa.ui.mechanic.home.HomeMechanicActivity
import com.automa.ui.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    @Inject lateinit var userData: DataStore<UserDataModel>

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            } else {
                checkLogin()
            }
        }

    private fun showNotificationPermissionRationale() {
        DialogUtils.showWhiteAlertDialog(
            this,
            "Notification Permission Required",
            "Notification permission is required, to show notification",
            positiveButton = Pair("OK") {
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }, negativeButton = Pair("Cancel") {

            })
    }

    private fun showSettingDialog() {
        DialogUtils.showWhiteAlertDialog(
            this,
            "Notification Permission Required",
            "Notification permission is required, to show notification",
            positiveButton = Pair("OK") {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:com.automa.app")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                checkLogin()
            }, negativeButton = Pair("Cancel") {
                checkLogin()
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            checkLogin()
        }
    }

    private fun checkLogin() {
        lifecycleScope.launchWhenCreated {
            delay(1500L)
            val data = userData.data.first()
            if (data.token.isNotBlank()) {
                when (data.roleType) {
                    UserRoleType.DRIVER -> {
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    }
                    UserRoleType.MECHANIC, UserRoleType.HEAD_MECHANIC -> {
                        startActivity(Intent(this@SplashActivity, HomeMechanicActivity::class.java))
                    }
                    UserRoleType.LIVE_MONITORING -> {
                        startActivity(Intent(this@SplashActivity, LiveMonitoringActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                }
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}