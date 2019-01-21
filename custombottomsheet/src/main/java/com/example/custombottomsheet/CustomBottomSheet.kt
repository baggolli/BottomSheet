package com.example.custombottomsheet

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.util.DisplayMetrics
import android.view.View
import android.widget.RelativeLayout

class CustomBottomSheet : BottomSheetDialogFragment(){
    var customBottomSheetListener: CustomBottomSheetListener? = null

    interface CustomBottomSheetListener {
        fun getCustomBottomSheetListener(childView: View?, dialog: Dialog?)
    }

    fun setBottomSheetListener(listener: CustomBottomSheetListener){
        customBottomSheetListener = listener
    }

    companion object {
        const val LAYOUT_ID = "LAYOUT_ID"
        const val PARENT_BACKGROUND_COLOR = "PARENT_BACKGROUND_COLOR"
        const val PARENT_HEIGHT = "PARENT_HEIGHT"

        fun newInstance(layoutId: Int,backgroundColor: Int,percentHeight: Int): CustomBottomSheet {
            val args = Bundle()
            args.apply {
                putInt(LAYOUT_ID,layoutId)
                putInt(PARENT_BACKGROUND_COLOR,backgroundColor)
                putInt(PARENT_HEIGHT,percentHeight)
            }

            val fragment = CustomBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        val inflatedView = View.inflate(context, R.layout.custom_bottom_sheet_dialog,null)
        dialog?.setContentView(inflatedView)
        val parent = inflatedView?.parent as View

        handleBottomSheetActionEvents(inflatedView)
        setUpBottomSheet(parent,arguments?.getInt(PARENT_BACKGROUND_COLOR))

        val childView = arguments?.getInt(LAYOUT_ID)?.let { View.inflate(context,it,null) }
        val nestedScrollView = dialog?.findViewById<NestedScrollView>(R.id.nestedScrollView)
        nestedScrollView?.addView(childView)
        customBottomSheetListener?.getCustomBottomSheetListener(childView,dialog)
    }

    private fun setUpBottomSheet(parent: View, backgroundColor: Int?) {
        val parentBottomSheetView = BottomSheetBehavior.from(parent)
        backgroundColor?.let {
            parent.setBackgroundColor(it)
        }

        parentBottomSheetView.peekHeight = calculateParentViewHeight()

        val bottomSheetView = dialog.findViewById<RelativeLayout>(R.id.bottom_sheet_view)
        bottomSheetView?.setBackground(ContextCompat.getDrawable(activity, R.drawable.bottom_sheet_rounded_corner))
        val behavior = BottomSheetBehavior.from(bottomSheetView)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState){
                    BottomSheetBehavior.STATE_HIDDEN -> dismiss()
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        calculateMinimumPeekHeight(behavior)
    }

    private fun calculateMinimumPeekHeight(behavior: BottomSheetBehavior<RelativeLayout>?) {
        arguments?.getInt(PARENT_HEIGHT)?.let {
            behavior?.peekHeight = Math.round((calculateParentViewHeight()*it) / (100).toDouble()).toInt()
        }
    }

    private fun handleBottomSheetActionEvents(inflatedView: View?) {
        inflatedView?.apply {
            setOnTouchListener{v, event -> dialog.setCancelable(false);false}
            setOnClickListener{dismiss()}
        }
    }

    private fun calculateParentViewHeight(): Int {
        val displayMatrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMatrics)
        return displayMatrics.heightPixels
    }
}