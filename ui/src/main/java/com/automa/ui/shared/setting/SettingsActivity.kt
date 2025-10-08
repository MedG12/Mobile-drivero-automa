package com.automa.ui.shared.setting

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.datastore.core.DataStore
import com.automa.datastore.pref.Preferences
import com.automa.datastore.pref.PreferencesConstant
import com.automa.datastore.user_data.UserDataModel
import com.automa.datastore.user_data.UserRoleType
import com.automa.ui.R
import com.automa.ui.base.BaseActivity
import com.automa.ui.databinding.ActivitySettingsBinding
import com.automa.ui.shared.scanner.ScannerActivity
import com.automa.ui.utils.DialogUtils
import com.automa.ui.utils.setItems
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private var languages = listOf<String>()
    private var showData = listOf<String>()

    private var currentLanguage = ""
    private var newLanguage = ""

    private var currentShowData = 0
    private var newShowData = 0
    @Inject lateinit var pref: Preferences
    @Inject lateinit var userData: DataStore<UserDataModel>

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startScannerActivityForResult.launch(ScannerActivity.newIntentForPairingDriver(this))
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

    private val startScannerActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLabelText(this)
        currentShowData = pref.getInt(PreferencesConstant.SHOW_DATA, PreferencesConstant.DEFAULT_SHOW_DATA)
        currentLanguage = pref.getString(PreferencesConstant.LANGUAGE, "id")
        newShowData = currentShowData
        newLanguage = currentLanguage

        getStrings(this)
        setData()

//        runBlocking {
//            if (userData.data.first().roleType == UserRoleType.MECHANIC || userData.data.first().roleType == UserRoleType.HEAD_MECHANIC) {
//                binding.tvLabelLanguage.isGone = true
//                binding.tilLanguage.isGone = true
//            }
//        }

        binding.toolbarSetting.setOnNavigationClickListener { finish() }

        binding.edtShowData.setOnClickListener {
            binding.edtShowData.showDropDown()
        }
        binding.edtLanguage.setOnClickListener {
            binding.edtLanguage.showDropDown()
        }

        binding.edtLanguage.setOnItemClickListener { _, _, i, _ ->
            parseLanguage(i)
        }

        binding.edtShowData.setOnItemClickListener { _, _, i, _ ->
            parseShowData(i)
        }

        binding.edtShowData.setText(convertShowDataToString(currentShowData), false)
        binding.edtLanguage.setText(convertLocaleToLanguageString(currentLanguage), false)

        binding.btnSave.setOnClickListener {
            saveData()
        }

        runBlocking {
            if (userData.data.first().roleType == UserRoleType.DRIVER) {
                binding.toolbarSetting.setRightIcons(
                    listOf(Pair(ContextCompat.getDrawable(this@SettingsActivity, R.drawable.ic_button_scan_qr)) {
                        checkPermissions()
                    })
                )
            }
        }
    }

    private fun setLabelText(context: Context) {
        binding.tvLabelLanguage.text = context.getString(R.string.label_language)
        binding.tvLabelShowData.text = context.getString(R.string.label_show_data)
    }

    private fun saveData() {
        pref.saveInt(PreferencesConstant.SHOW_DATA, newShowData)
        pref.saveString(PreferencesConstant.LANGUAGE, newLanguage)

        finish()
    }

    private fun parseShowData(i: Int) {
        newShowData = showData[i].dropLast(5).toInt()
    }

    private fun parseLanguage(i: Int) {
        newLanguage = if (i==0) "id" else "en"
        val newContext = setNewLanguage(newLanguage)
        onLanguageChanged(newContext)
    }

    private fun convertLocaleToLanguageString(locale: String): String {
        return if (locale == "id") {
            languages[0]
        } else {
            languages[1]
        }
    }

    private fun convertShowDataToString(days: Int): String {
        return when (days) {
            1 -> showData[0]
            7 -> showData[1]
            14 -> showData[2]
            30 -> showData[3]
            else -> showData[0]
        }
    }

    private fun getStrings(context: Context) {
        languages = context.resources.getStringArray(R.array.language).toList()
        showData = context.resources.getStringArray(R.array.show_data).toList()
    }

    private fun setData() {
        binding.edtLanguage.setItems(languages.toMutableList())
        binding.edtShowData.setItems(showData.toMutableList())
    }

    private fun onLanguageChanged(context: Context) {
        getStrings(context)
        setData()

        binding.edtShowData.setText(convertShowDataToString(newShowData), false)
        binding.edtLanguage.setText(convertLocaleToLanguageString(newLanguage), false)

        setLabelText(context)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            startScannerActivityForResult.launch(ScannerActivity.newIntentForPairingDriver(this))
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
}