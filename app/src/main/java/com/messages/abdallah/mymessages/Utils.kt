package com.messages.abdallah.mymessages

import android.content.Context
import android.content.Intent
import android.util.Log

class Utils {

    companion object {
        fun IntenteShare(con: Context, dialog_Heder_text: String, heder: String, Msg: String) {
            try {

                var sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.setType("text/plain")
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heder)
                sharingIntent.putExtra(Intent.EXTRA_TEXT, Msg)
                con.startActivity(
                    Intent.createChooser(
                        sharingIntent,
                        dialog_Heder_text
                    )
                )
            } catch (e: Exception) {
                Log.d("error in share", e.toString())
            }
        }
    }







}

