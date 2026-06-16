package com.example.citroensplash

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        prefs = Prefs(this)

        val enableSwitch = findViewById<Switch>(R.id.switchEnabled)
        val durationSeek = findViewById<SeekBar>(R.id.seekDuration)
        val durationLabel = findViewById<TextView>(R.id.labelDuration)
        val saveButton = findViewById<Button>(R.id.buttonSave)

        val range = Prefs.MAX_DURATION_SECONDS - Prefs.MIN_DURATION_SECONDS
        durationSeek.max = range

        enableSwitch.isChecked = prefs.isEnabled
        durationSeek.progress = prefs.durationSeconds - Prefs.MIN_DURATION_SECONDS
        durationLabel.text = getString(R.string.duration_label, prefs.durationSeconds)
        durationSeek.isEnabled = enableSwitch.isChecked

        enableSwitch.setOnCheckedChangeListener { _, isChecked ->
            durationSeek.isEnabled = isChecked
        }

        durationSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                durationLabel.text = getString(R.string.duration_label, progress + Prefs.MIN_DURATION_SECONDS)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        saveButton.setOnClickListener {
            prefs.isEnabled = enableSwitch.isChecked
            prefs.durationSeconds = durationSeek.progress + Prefs.MIN_DURATION_SECONDS
            finish()
        }
    }
}
