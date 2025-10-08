package com.automa.ui.mechanic.home

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.automa.datastore.pref.Preferences
import com.automa.datastore.pref.PreferencesConstant
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.common.CommonPagerAdapter
import com.automa.ui.databinding.ActivityHomeMechanicBinding
import com.automa.ui.driver.home.NotifFragment
import com.automa.ui.driver.storing.StoringRequestActivity
import com.automa.ui.shared.deep_link.DeepLinkActivity
import com.automa.ui.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeMechanicActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeMechanicBinding

    private var language = "id"
    lateinit var toggleDrawer: ActionBarDrawerToggle
    @Inject lateinit var pref: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language = pref.getString(PreferencesConstant.LANGUAGE, "id")

        binding = ActivityHomeMechanicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initAction()
    }

    override fun onResume() {
        super.onResume()
        val newLanguage = pref.getString(PreferencesConstant.LANGUAGE, "id")
        if (language!=newLanguage) {
            recreate()
        }
    }

    private fun initUi() {
        val myFabSrc = ContextCompat.getDrawable(this, R.drawable.ic_warning)
        val willBeWhite = myFabSrc?.constantState?.newDrawable()
        willBeWhite?.mutate()?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
        binding.fabService.setImageDrawable(willBeWhite)

        toggleDrawer = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.title_activity_drawer, R.string.title_activity_drawer)
        binding.drawerLayout.addDrawerListener(toggleDrawer)
        toggleDrawer.syncState()

        val listFragment = listOf(
            HomeMechanicFragment(),
            DashboardMechanicFragment(),
            NotifFragment(),
            AccountMechanicFragment())
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
            startActivity(Intent(this, StoringRequestActivity::class.java))
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
}