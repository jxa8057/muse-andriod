package com.example.finalproject.components

import android.app.Dialog
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.activities.PlayRecordingActivity
import kotlinx.android.synthetic.main.fragment_rename_recording.view.*
import java.io.File

class RenameDialog: DialogFragment() {

    companion object {
        const val ARG_NAME = "name"

        fun newInstance(name: String): RenameDialog {
            val dialog = RenameDialog()
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
            builder.setTitle("Rename")
            builder.setMessage("Choose a name for your recording.")
            val renameView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_rename_recording, null)
            builder.setView(renameView)
            builder.setPositiveButton("Rename") { _, _ ->
                renameFile(renameView.renameET.text.toString())
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun renameFile(newName: String) {
        if (!newName.isBlank()) {
            val oldName = arguments?.getString(ARG_NAME)
            val externalStoragePath = Environment.getExternalStorageDirectory().absolutePath + "/muse/"
            val newFile = File("$externalStoragePath$newName.mp3")
            val oldFile = File("$externalStoragePath$oldName.mp3")
            if (oldFile.exists() && !newFile.exists()) {
                oldFile.renameTo(newFile)
            } else {
                Toast.makeText(requireContext(), "something went wrong when renaming the file.", Toast.LENGTH_LONG).show()
            }
        }
        if (requireActivity() is PlayRecordingActivity) {
            requireActivity().finish()
        }
    }
}