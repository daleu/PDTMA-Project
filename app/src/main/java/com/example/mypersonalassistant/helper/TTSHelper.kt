
package com.example.mypersonalassistant.helper

import android.app.Activity
import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.widget.Toast
import java.util.*

class TTSHelper(private val context: Context,
          private val message: String) : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(context, this)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInit(i: Int) {
        if (i == TextToSpeech.SUCCESS) {

            val localeUS = Locale.US

            val result: Int
            result = tts.setLanguage(localeUS)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "This Language is not supported", Toast.LENGTH_SHORT).show()
            } else {
                speakOut(message)
            }

        } else {
            Toast.makeText(context, "Initilization Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun speakOut(message: String) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
