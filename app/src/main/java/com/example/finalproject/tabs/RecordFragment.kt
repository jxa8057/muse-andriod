package com.example.finalproject.tabs

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject.R
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.example.finalproject.components.RenameDialog
import kotlinx.android.synthetic.main.tab1record.*
import java.io.File
import java.io.IOException

class RecordFragment : Fragment() {

    private var outputFilePath: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var recordingName: String = "recording"
    private var isRecording: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab1record, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        checkPermissions()

        recordButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    private fun checkPermissions() {
        // Check if the user gave permission to use audio recorder.
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions,0)
        } else {
            setUpRecorder()
        }
    }

    private fun setUpRecorder() {
        val externalStoragePath = Environment.getExternalStorageDirectory().absolutePath + "/muse/"
        var folderExists = true
        if (!File(externalStoragePath).exists()) {
            folderExists = File(externalStoragePath).mkdir()
        }

        if (folderExists) {
            outputFilePath = "$externalStoragePath$recordingName.mp3"

            if (File(outputFilePath).exists()) {
                outputFilePath.let {
                    recordingName = "$recordingName(1)"
                    outputFilePath = "$externalStoragePath$recordingName.mp3"
                }
            }

            mediaRecorder = MediaRecorder()

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(outputFilePath)
        } else {
            Toast.makeText(requireContext(), "There was a problem accessing the Muse recordings folder on your device.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRecording() {
        setUpRecorder()
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
            recordButton.text = "Stop Recording"

            Toast.makeText(requireContext(), "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Recording failed to start (Illegal State).", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Recording failed to start (IOException).", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        if(isRecording){
            Toast.makeText(requireContext(), "Recording stopped!", Toast.LENGTH_SHORT).show()
            mediaRecorder?.stop()
            mediaRecorder?.release()
            isRecording = false
            recordButton.text = "Record"
            renameRecording()
        } else {
//            Toast.makeText(this, "Stop recording called when not recording...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renameRecording() {
        RenameDialog.newInstance(recordingName).show(activity?.supportFragmentManager, "Rename")
    }

    companion object {
        fun newInstance(): RecordFragment {
            return RecordFragment()
        }
    }
}
