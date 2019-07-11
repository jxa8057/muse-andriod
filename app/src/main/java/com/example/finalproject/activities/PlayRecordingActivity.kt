package com.example.finalproject.activities

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.Recording
import com.example.finalproject.components.DeleteDialog
import com.example.finalproject.components.RenameDialog
import kotlinx.android.synthetic.main.activity_play_recording.*

class PlayRecordingActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var recordingTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_recording)

        recordingTitle = intent.getStringExtra("title")
            ?: throw IllegalStateException("field $title missing in Intent")

        titleTV.text = recordingTitle

        setUpMediaPlayer()

        playPauseButton.setOnClickListener {
            onPlayPausePress()
        }

        mediaPlayer.setOnCompletionListener {
            Glide.with(this).load(R.drawable.ic_play_circle_outline_black_24dp).into(playPauseButton)
        }

    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.delete_action -> {
                DeleteDialog.newInstance(recordingTitle).show(supportFragmentManager, "Delete")
            }
            R.id.rename_action -> {
                RenameDialog.newInstance(recordingTitle).show(supportFragmentManager, "Rename")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setUpMediaPlayer() {
        mediaPlayer = MediaPlayer()

        try{
            mediaPlayer.setDataSource("${Environment.getExternalStorageDirectory().absolutePath}/muse/$recordingTitle.mp3")
            mediaPlayer.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onPlayPausePress() {
        if (mediaPlayer.isPlaying) {
            pause()
        } else {
            play()
        }

    }

    private fun play() {
        mediaPlayer.start()
        Glide.with(this).load(R.drawable.ic_pause_circle_outline_black_24dp).into(playPauseButton)

    }

    private fun pause() {
        mediaPlayer.pause()
        Glide.with(this).load(R.drawable.ic_play_circle_outline_black_24dp).into(playPauseButton)
    }

    companion object {
        fun newIntent(context: Context, recording: Recording): Intent {
            val intent = Intent(context, PlayRecordingActivity::class.java)
            intent.putExtra("title", recording.title)
            return intent
        }
    }
}
