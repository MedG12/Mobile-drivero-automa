package com.automa.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.automa.ui.common.CameraXActivity

class CameraActivityContract: ActivityResultContract<Int, String?>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, CameraXActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode == Activity.RESULT_OK) {
            return intent?.getStringExtra(CameraXActivity.OUTPUT_FILE_PATH)
        }
        return null
    }
}