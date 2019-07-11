package com.example.finalproject.tabs

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject.R
import com.example.finalproject.Recording
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import com.example.finalproject.RecyclerViewAdapter
import com.example.finalproject.activities.PlayRecordingActivity
import kotlinx.android.synthetic.main.tab3calendar.*
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class CalendarFragment : Fragment() {

    private lateinit var recordingAdapter: RecyclerViewAdapter<Recording>
    private var recordings = mutableListOf<Recording>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab3calendar, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recordingAdapter = RecyclerViewAdapter(listOf(), R.layout.recording_item) { rec, _ ->
            playRecording(rec)
        }

        with(calendarRV) {
            layoutManager = LinearLayoutManager(activity)
            adapter = recordingAdapter
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            var safeMonth = "${month + 1}"
            var safeDay = "$dayOfMonth"
            if (month < 10) {
                safeMonth = "0${month + 1}"
            }
            if (dayOfMonth < 10) {
                safeDay = "0$dayOfMonth"
            }
            val dateSelected = LocalDate.parse("$year-$safeMonth-$safeDay")
            onDateSelected(dateSelected)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            getRecordings()
            onDateSelected(LocalDate.now())
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
    }

    private fun playRecording(recording: Recording) {
        val playIntent = PlayRecordingActivity.newIntent(requireContext(), recording)
        startActivity(playIntent)
    }

    private fun onDateSelected(date: LocalDate) {
        val recordingsForDate = mutableListOf<Recording>()
        recordings.forEach {
            if (date.compareTo(it.date) == 0) {
                recordingsForDate.add(it)
            }
        }

        recordingAdapter.updateData(recordingsForDate.toList())

    }

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }
}
