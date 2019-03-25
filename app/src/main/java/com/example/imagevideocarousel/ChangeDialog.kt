package com.example.imagevideocarousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.imagevideocarousel.interfaces.AmountChangeListener
import kotlinx.android.synthetic.main.dialog_change.*


class ChangeDialog : DialogFragment() {

    var amountValue: Int = 0
    var amountChangeListener: AmountChangeListener? = null
    var contentView: View? = null


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window.setLayout(width, height)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(R.layout.dialog_change, container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header.text = arguments?.getString(HEADER_TEXT)
        confirmButton.setOnClickListener {
            amountChangeListener?.onChanged(changeInputLayout.editText?.text.toString().toInt())
            this@ChangeDialog.dismiss()
        }
    }

    fun setParams(amountChangeListener: AmountChangeListener) {
        this.amountChangeListener = amountChangeListener
    }

    companion object {
        val HEADER_TEXT = "HEADER_TEXT"
        val AMOUNT_VALUE = "AMOUNT_VALUE"

        fun newInstance(headerText: String, amountValue: Int): ChangeDialog {
            val bundle = Bundle(2)
            bundle.putString(HEADER_TEXT, headerText)
            bundle.putInt(AMOUNT_VALUE, amountValue)
            val dialog = ChangeDialog()
            dialog.arguments = bundle
            return dialog
        }
    }
}