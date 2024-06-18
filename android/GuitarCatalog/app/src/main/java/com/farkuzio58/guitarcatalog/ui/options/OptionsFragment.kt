/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Fragment para configurar la App y descargar los manuales de la misma.
 */

package com.farkuzio58.guitarcatalog.ui.options

import android.R
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.farkuzio58.guitarcatalog.databinding.FragmentOptionsBinding
import com.farkuzio58.guitarcatalog.ui.BaseFragmentDialog
import com.farkuzio58.guitarcatalog.ui.DeleteDataFragmentDialog
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

class OptionsFragment : Fragment(), BaseFragmentDialog.DialogListener {

    var _binding: FragmentOptionsBinding? = null
    val binding get() = _binding!!
    var document: Int = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        activity?.title = getString(com.farkuzio58.guitarcatalog.R.string.options)
        val languages = listOf("Español", "English", "Deutsch")
        val codes = listOf("es", "en", "de")
        mediaPlayer = MediaPlayer.create(requireContext(), com.farkuzio58.guitarcatalog.R.raw.guitar)

        val myAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, languages)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = myAdapter

        val index = codes.indexOf(context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)?.getString("lan", Locale.getDefault().language))

        binding.spinner.setSelection(index)

        val sharedPreferences = context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)
        binding.tvCurrentIp.text = sharedPreferences?.getString("serverIp", "192.168.1.58")

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val language = parent!!.selectedItemPosition
                if(sharedPreferences?.getString("lan", Locale.getDefault().language) != codes[language]){
                    sharedPreferences?.edit()?.putString("lan", codes[language])?.apply()
                    setLocale(codes[language])
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.lnlIp.setOnClickListener{
            val dialog = BaseFragmentDialog()
            dialog.setDialogListener(this)
            dialog.show(parentFragmentManager, "BaseFragmentDialog")
        }

        binding.cvAdmin.setOnClickListener{
            Toast.makeText(requireContext(), getString(com.farkuzio58.guitarcatalog.R.string.downloading), Toast.LENGTH_SHORT).show()
            document = 0
            downloadPdf()
        }
        binding.cvUser.setOnClickListener{
            Toast.makeText(requireContext(), getString(com.farkuzio58.guitarcatalog.R.string.downloading), Toast.LENGTH_SHORT).show()
            document = 1
            downloadPdf()
        }

        binding.cvDelete.setOnClickListener{
            try{
                DeleteDataFragmentDialog().show(parentFragmentManager, "BaseFragmentDialog")
            }
            catch (e: Exception){

            }
        }
        binding.imageView3.setOnClickListener{
            mediaPlayer?.start()
        }
        return binding.root
    }

    private fun downloadPdf() {
        val files = listOf("guitar_catalog_admin_documentation", "guitar_catalog_user_documentation")
        val inputStream: InputStream = if(document == 0)
            resources.openRawResource(com.farkuzio58.guitarcatalog.R.raw.guitar_catalog_admin_documentation)
        else
            resources.openRawResource(com.farkuzio58.guitarcatalog.R.raw.guitar_catalog_user_documentation)
        val fileName = "${files[document]}.pdf"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val resolver = requireContext().contentResolver
        val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
                val outputStream: OutputStream? = resolver.openOutputStream(uri)
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream?.write(buffer, 0, length)
                }
                outputStream?.close()
                inputStream.close()
                Toast.makeText(requireContext(), getString(com.farkuzio58.guitarcatalog.R.string.downloadSuccess), Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), getString(com.farkuzio58.guitarcatalog.R.string.downloadFail), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), getString(com.farkuzio58.guitarcatalog.R.string.downloadFail), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 112) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPdf()
            } else {
                Toast.makeText(requireContext(), getString(com.farkuzio58.guitarcatalog.R.string.downloadRequest), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
    override fun onDialogClosed() {
        val sharedPreferences = context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)
        binding.tvCurrentIp.text = sharedPreferences?.getString("serverIp", "192.168.1.58")
        activity?.title = getString(com.farkuzio58.guitarcatalog.R.string.options)
    }
}