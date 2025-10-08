package com.automa.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.automa.datastore.di.PrefModule
import com.automa.datastore.pref.PreferencesConstant
import com.automa.ui.custom_component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import java.util.*

@AndroidEntryPoint
open class BaseActivity: AppCompatActivity() {
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(this)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(wrapContext(newBase))
    }

    private fun wrapContext(context: Context): Context {
        val lang = EntryPointAccessors.fromApplication(context, PrefModule.AppPrefProvider::class.java).getPreferences().getString(PreferencesConstant.LANGUAGE, "id")
        val savedLocale = Locale(lang)
        Locale.setDefault(savedLocale)
        val newConf = Configuration().apply {
            setLocale(savedLocale)
        }
        return context.createConfigurationContext(newConf)
    }

    private fun wrapContext(context: Context, language: String): Context {
        val savedLocale = Locale(language)
        Locale.setDefault(savedLocale)
        val newConf = Configuration().apply {
            setLocale(savedLocale)
        }
        return context.createConfigurationContext(newConf)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setNewLanguage(language: String): Context {
        return wrapContext(baseContext, language)
    }
}