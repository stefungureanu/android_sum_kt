package ro.pub.cs.systems.eim.colocviu1_2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class Colocviu1_2SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val int1 : String = intent.getStringExtra("input1").toString()

        val userInput = int1.split(" ")
        var sum = userInput[0].toInt()

        for (i in userInput.indices) {
            if (userInput[i] == "+") {
                sum += userInput[i + 1].toInt()
            }
        }

        val intent = Intent()
        intent.putExtra("sum", sum)
        setResult(RESULT_OK, intent)
        finish()


    }
}