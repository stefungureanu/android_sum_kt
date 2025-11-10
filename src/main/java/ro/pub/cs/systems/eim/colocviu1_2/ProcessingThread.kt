package ro.pub.cs.systems.eim.colocviu1_2

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Random
import kotlin.math.sqrt



class ProcessingThread(private val context: Context, private val sum: Int) :
    Thread() {
    private var isRunning = true

    private val random = Random()



    override fun run() {
        Log.d(
            "THREAD_PROC",
            "Thread has started! PID: " + Process.myPid() + " TID: " + Process.myTid()
        )
        sendMessage()
        Log.d("THREAD_PROC", "Thread has stopped!")
    }

    private fun sendMessage() {
        var answer = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
            answer =  current.format(formatter)
            Log.d("answer",answer)
        } else {
            var date = Date()
            val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
            answer = formatter.format(date)
            Log.d("answer",answer)
        }

        answer = answer + sum.toString()
        val intent = Intent()
        intent.setAction("ProcessingThread")
        intent.putExtra("input1", answer)

        context.sendBroadcast(intent)
    }


    fun stopThread() {
        isRunning = false
    }
}