package com.automa.ui.custom_component

import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.automa.ui.databinding.CustomIncidentImagePopupBinding

class CustomIncidentImagePopupWindow(private val parentView: View) {
    private var popupLayout: CustomIncidentImagePopupBinding ?= null
    private var popupWindow = PopupWindow()

    private var anchorView: View ?= null
    private var actionEdit: (()->Unit) ?= null
    private var actionDelete: (()->Unit) ?= null

    init {
        popupLayout = CustomIncidentImagePopupBinding.inflate(LayoutInflater.from(parentView.context), parentView as ViewGroup, false)
    }

    fun init(actionEdit: ()->Unit, actionDelete: ()-> Unit, anchorView: View): CustomIncidentImagePopupWindow {
        this.actionEdit = actionEdit
        this.actionDelete = actionDelete
        this.anchorView = anchorView
        return this
    }

    fun show() {
        popupLayout?.llEdit?.setOnClickListener {
            actionEdit?.invoke()
            popupWindow.dismiss()
        }
        popupLayout?.llDelete?.setOnClickListener {
            actionDelete?.invoke()
            popupWindow.dismiss()
        }

        val params = popupLayout?.root?.layoutParams
        popupWindow.apply {
            contentView = popupLayout?.root
            width = params?.width ?: 0
            height = params?.height ?: 0
            isFocusable = true
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            elevation = 2f
            isOutsideTouchable = true
            setBackgroundDrawable(BitmapDrawable(null, ""))
        }

        val position = IntArray(2)
        anchorView?.getLocationOnScreen(position)

        val xPosition = position[0]
        val yPosition = position[1]
        val windowWidth = popupWindow.contentView.measuredWidth
        val windowHeight = popupWindow.contentView.measuredHeight
        val posX = xPosition + windowWidth
        val posY = yPosition + (windowHeight/2)
        popupWindow.showAtLocation(
            parentView,
            Gravity.NO_GRAVITY,
            posX, posY
        )
    }
}