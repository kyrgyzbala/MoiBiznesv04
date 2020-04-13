package com.kyrgyzcoder.moibiznesv04.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.kyrgyzcoder.moibiznesv04.R
import com.kyrgyzcoder.moibiznesv04.editadd.EditAddActivity
import kotlinx.android.synthetic.main.sell_dialog.view.*

class SellDialog : AppCompatDialogFragment() {

    private lateinit var listener: SellDialogListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater: LayoutInflater = activity!!.layoutInflater
        val view: View = inflater.inflate(R.layout.sell_dialog, null)
        builder.setView(view)
            .setTitle("Продажа товара")
            .setNegativeButton("Отмена") { _, _ ->
                Toast.makeText(activity, "Продажа отменено", Toast.LENGTH_SHORT).show()
            }
        builder.setPositiveButton("Продать") { _, _ ->
            val quantity = view.editTextSold.text.toString().trim()
            if (Integer.valueOf(quantity) <= EditAddActivity.quantityG && Integer.valueOf(quantity) >= 0) {
                Toast.makeText(activity, "Продано $quantity штук", Toast.LENGTH_SHORT).show()
                listener.setQuantity(quantity, true)
            } else if (Integer.valueOf(quantity) < 0) {
                Toast.makeText(
                    activity,
                    "Вводите правильное количество! Попробуйте еще раз!",
                    Toast.LENGTH_SHORT
                ).show()
                listener.setQuantity(quantity, false)
            } else {
                Toast.makeText(
                    activity,
                    "У вас не достаточно товара! Попробуйте еще раз!",
                    Toast.LENGTH_SHORT
                ).show()
                listener.setQuantity(quantity, false)
            }
        }
        return builder.create().also {
            it.window!!.setBackgroundDrawableResource(R.color.colorPrimary)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as SellDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "Error! Must Implement Activity")
        }
    }


    interface SellDialogListener {
        fun setQuantity(quantity: String, prodano: Boolean)
    }
}