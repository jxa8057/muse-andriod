package com.example.finalproject.components

import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.Toast
import java.io.File

class DeleteDialog: DialogFragment() {

    companion object {
        const val ARG_NAME = "name"

        fun newInstance(name: String): DeleteDialog {
            val dialog = DeleteDialog()
            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
            }
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure you want to delete this recording?")
            builder.setPositiveButton("Delete") { _, _ ->
                deleteFile()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun deleteFile() {
        val name = arguments?.getString(ARG_NAME)
        val externalStoragePath = Environment.getExternalStorageDirectory().absolutePath + "/muse/"
        val file = File("$externalStoragePath$name.mp3")
        if (file.exists()) {
            file.delete()
        } else {
            Toast.makeText(requireContext(), "There was an issue deleting the specified file.", Toast.LENGTH_LONG).show()
        }
        requireActivity().finish()
    }
}