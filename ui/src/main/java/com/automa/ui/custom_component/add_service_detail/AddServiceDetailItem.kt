package com.automa.ui.custom_component.add_service_detail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.automa.ui.R
import com.automa.ui.databinding.CustomAddServiceDetailItemBinding
import com.automa.ui.utils.DialogUtils

class AddServiceDetailItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    private val binding = CustomAddServiceDetailItemBinding.inflate(LayoutInflater.from(context), this, true)

    fun initView(action: ()-> Unit) {
        binding.ivDeleteServiceActivity.setOnClickListener {
            DialogUtils.showDialogInfoWithImage(context,
                R.drawable.illustration_delete_activity,
                context.getString(R.string.title_delete_service_activity),
                context.getString(R.string.message_delete_service_activity),
                positiveButton = Pair(context.getString(R.string.button_delete)) {
                    action.invoke()
                }, negativeButton = Pair(context.getString(R.string.cancel)) {

                }
            )
        }
    }
}