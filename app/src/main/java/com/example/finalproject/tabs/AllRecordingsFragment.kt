package com.example.finalproject.tabs

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject.R
import com.example.finalproject.Recording
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.example.finalproject.RecyclerViewAdapter
import com.example.finalproject.activities.PlayRecordingActivity
import kotlinx.android.synthetic.main.tab2allrecordings.*
import java.io.File
import java.time.Instant
import java.time.ZoneId

class AllRecordingsFragment : Fragment() {

    private lateinit var recordingAdapter: RecyclerViewAdapter<Recording>
    private var recordings = mutableListOf<Recording>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab2allrecordings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recordingAdapter = RecyclerViewAdapter(listOf(), R.layout.recording_item) { rec, _ ->
            playRecording(rec)
        }

        val itemDecor = DividerItemDecoration(context, HORIZONTAL)

        with(allRecordingsRV) {
            this.addItemDecoration(itemDecor)
            layoutManager = LinearLayoutManager(activity)
            adapter = recordingAdapter
        }

        getRecordings()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            getRecordings()
        }
    }

    private fun getRecordings() {

        recordings.clear()

        val recordingsPath = Environment.getExternalStorageDirectory().absolutePath + "/muse/"
        File(recordingsPath).walkBottomUp().forEach {
            if (it.isFile) {
                val modifiedDate = Instant.ofEpochMilli(it.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate()
                val recording = Recording(it.nameWithoutExtension, modifiedDate)
                recordings.add(recording)
            }
        }

        recordingAdapter.updateData(recordings.toList())

    }

    private fun playRecording(recording: Recording) {
        val playIntent = PlayRecordingActivity.newIntent(requireContext(), recording)
        startActivity(playIntent)
    }

    companion object {
        fun newInstance(): AllRecordingsFragment {
            return AllRecordingsFragment()
        }
    }
}
