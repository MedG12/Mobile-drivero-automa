package com.automa.ui.driver.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.automa.datastore.pref.Preferences
import com.automa.datastore.pref.PreferencesConstant
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.common.CommonPagerAdapter
import com.automa.ui.databinding.ActivityHomeBinding
import com.automa.ui.shared.scanner.ScannerActivity
import com.automa.ui.mechanic.breakdown_report.BreakdownReportActivity
import com.automa.ui.shared.deep_link.DeepLinkActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    lateinit var toggleDrawer: ActionBarDrawerToggle

    val homeViewModel: HomeViewModel by viewModels()
    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private var language = "id"

    @Inject lateinit var pref: Preferences

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startActivity(Intent(this, ScannerActivity::class.java))
        } else {
            DialogUtils.showWhiteAlertDialog(
                this,
                getString(R.string.message_permission_needed),
                getString(R.string.message_request_permission),
                positiveButton = Pair(getString(R.string.ok)) {

                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val newLanguage = pref.getString(PreferencesConstant.LANGUAGE, "id")
        if (language!=newLanguage) {
            recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language = pref.getString(PreferencesConstant.LANGUAGE, "id")

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initAction()
    }

    private fun initUi() {
        val myFabSrc = ContextCompat.getDrawable(this, R.drawable.ic_warning)
        val willBeWhite = myFabSrc?.constantState?.newDrawable()
        willBeWhite?.mutate()?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
        binding.fabService.setImageDrawable(willBeWhite)

        toggleDrawer = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.title_activity_drawer, R.string.title_activity_drawer)
        binding.drawerLayout.addDrawerListener(toggleDrawer)
        toggleDrawer.syncState()

        val listFragment = listOf(HomeFragment(), DashboardFragment(), NotifFragment(), AccountFragment())
        binding.vpHome.apply {
            adapter = CommonPagerAdapter(listFragment, supportFragmentManager, lifecycle)
            isUserInputEnabled = false
            offscreenPageLimit = listFragment.size
        }
        processPendingDeepLink()
    }

    private fun processPendingDeepLink() {
        val pending = pref.getString(PreferencesConstant.PENDING_DEEPLINK_TYPE)
        val pendingValue = pref.getString(PreferencesConstant.PENDING_DEEPLINK_VALUE)
        if (pending.isNotEmpty()) {
            when (pending) {
                DeepLinkActivity.DIRECTION_ACCOUNT -> {
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_TYPE, "")
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_VALUE, "")
                    binding.bottomNavMain.selectedItemId = R.id.action_account
                    binding.vpHome.setCurrentItem(3, false)
                }
                DeepLinkActivity.DIRECTION_NOTIF -> {
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_TYPE, "")
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_VALUE, "")
                    binding.bottomNavMain.selectedItemId = R.id.action_notif
                    binding.vpHome.setCurrentItem(2, false)
                }
                DeepLinkActivity.DIRECTION_TASK -> {
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_TYPE, "")
                    pref.saveString(PreferencesConstant.PENDING_DEEPLINK_VALUE, "")
                    binding.bottomNavMain.selectedItemId = R.id.action_home
                    binding.vpHome.setCurrentItem(0, false)
                }
            }
        }
    }

    private fun initAction() {
        binding.toolbarHome.setOnShowMenuClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            binding.navView.bringToFront()
        }

        binding.fabService.setOnClickListener {
            startActivity(Intent(this, BreakdownReportActivity::class.java))
//            checkPermissions()
        }

        binding.bottomNavMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    binding.vpHome.setCurrentItem(0, false)
                    return@setOnItemSelectedListener true
                }
//                R.id.action_dashboard -> {
//                    binding.vpHome.setCurrentItem(1, false)
//                    return@setOnItemSelectedListener true
//                }
                R.id.action_notif -> {
                    binding.vpHome.setCurrentItem(2, false)
                    return@setOnItemSelectedListener true
                }
                R.id.action_account -> {
                    binding.vpHome.setCurrentItem(3, false)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menudrawer1 -> toast(this, "menu1")
                R.id.menudrawer2 -> toast(this, "menu2")
            }
            false
        }
    }


    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this, ScannerActivity::class.java))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(CAMERA_PERMISSION)) {
                DialogUtils.showWhiteAlertDialog(
                    this,
                    getString(R.string.message_permission_needed),
                    getString(R.string.message_request_permission),
                    positiveButton = Pair(getString(R.string.ok)) {
                        requestPermissionLauncher.launch(CAMERA_PERMISSION)
                    },
                    negativeButton = Pair(getString(R.string.cancel)) {

                    }
                )
            } else {
                requestPermissionLauncher.launch(CAMERA_PERMISSION)
            }
        } else {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }
    }

    fun backToHome() {
        binding.bottomNavMain.selectedItemId = R.id.action_home
        binding.vpHome.setCurrentItem(0, false)
    }
}