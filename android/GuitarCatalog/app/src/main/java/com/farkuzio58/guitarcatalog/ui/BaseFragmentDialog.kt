/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Dialog para cambiar la ip del servidor.
 */

package com.farkuzio58.guitarcatalog.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.DialogFragment
import com.farkuzio58.guitarcatalog.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class BaseFragmentDialog() : DialogFragment() {

    interface DialogListener {
        fun onDialogClosed()
    }

    private var dialogListener: DialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialAlertDialog)
        builder.setTitle(getString(R.string.bfdAttention))

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_tiet, null)
        val tieIpServer = view.findViewById<TextInputEditText>(R.id.tieIpServer)

        val message =
            SpannableString(getString(R.string.bfdMessage))
        message.setSpan(RelativeSizeSpan(1.3f), 0, message.length, 0)
        builder.setMessage(message)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val ip = tieIpServer.text.toString()
                val sharedPreferences = context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)
                if(!ip.isNullOrEmpty())
                    sharedPreferences?.edit()?.putString("serverIp", ip)?.apply()
                else
                    sharedPreferences?.edit()?.putString("serverIp", "192.168.1.58")?.apply()

                dialogListener?.onDialogClosed()
            }
            .setNegativeButton(android.R.string.cancel)
            { _, _ ->
                dialogListener?.onDialogClosed()
            }
        return builder.create()
    }

    fun setDialogListener(listener: DialogListener) {
        this.dialogListener = listener
    }
}