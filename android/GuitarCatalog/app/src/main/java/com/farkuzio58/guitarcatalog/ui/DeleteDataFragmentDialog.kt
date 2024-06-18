/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Dialog para confirmar el borrado de datos.
 */

package com.farkuzio58.guitarcatalog.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.DialogFragment
import com.farkuzio58.guitarcatalog.R
import com.farkuzio58.guitarcatalog.database.IdRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteDataFragmentDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialAlertDialog)
        builder.setTitle(getString(R.string.bfdAttention))

        val message =
            SpannableString(getString(R.string.ddfdMessage))
        message.setSpan(RelativeSizeSpan(1.3f), 0, message.length, 0)
        builder.setMessage(message)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                try{
                    context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)?.edit()?.clear()?.commit()

                    IdRepository().deleteAll()
                    val context = requireContext()
                    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        activity?.finish()
                        Runtime.getRuntime().exit(0)
                    }
                }
                catch (e: Exception){

                }
            }
            .setNegativeButton(android.R.string.cancel)
            { _, _ ->
            }

        return builder.create()
    }
}