package ro.pub.cs.systems.eim.colocviu1_2

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Colocviu1_2MainActivity : AppCompatActivity() {
    private lateinit var numbereditText: EditText
    private lateinit var numberText: TextView
    private lateinit var setButton: Button
    private var sum: Int = -1
    private lateinit var answer: String

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                answer = intent.getStringExtra("input1").toString()
                toastdate(answer)
            }
        }
    }

    fun toastdate(answer: String) {
        Toast.makeText(this, answer, Toast.LENGTH_LONG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_2_main)

        val serviceIntent = Intent(this, Colocviu1_2Service::class.java)
        startService(serviceIntent)

        numbereditText = findViewById(R.id.input1)
        numberText = findViewById(R.id.input2)


        val addButton = findViewById<Button>(R.id.add)
        addButton.setOnClickListener {
            var ok = true
            if (numbereditText.text.toString().toIntOrNull() == null)
                ok = false
            if (ok) {
                if (numberText.text.toString().isEmpty())
                    numberText.setText(numbereditText.text.toString())
                else
                    numberText.setText(numberText.text.toString() + " + " + numbereditText.text.toString())
            }
        }

        val activityResultsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val sumResult = result.data?.getIntExtra("sum", 0)
                val prodResult = result.data?.getIntExtra("prod", 0)

                // Salvează doar rezultatele care nu sunt 0
                if (sumResult != null && sumResult != -1) {
                    sum = sumResult
                    Toast.makeText(this, "Suma salvată: $sumResult", Toast.LENGTH_LONG).show()
                }

                if (sum > 10)
                    startServiceIfConditionIsMet()


//                if (prodResult != null && prodResult != -1) {
//                    prod = prodResult
//                    Toast.makeText(this, "Produsul salvat: $prod", Toast.LENGTH_LONG).show()
//                }
            }
        }

        val computeButton = findViewById<Button>(R.id.compute)
        computeButton.setOnClickListener {
            val intent = Intent(this, Colocviu1_2SecondaryActivity::class.java)
            intent.putExtra("input1", numberText.text)


            activityResultsLauncher.launch(intent)
        }
    }


    private fun startServiceIfConditionIsMet() {
        if (sum > 10) {
            val intent = Intent(applicationContext, Colocviu1_2Service::class.java).apply {
                putExtra("sum", sum)
            }
            applicationContext.startService(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        // din processing thread "ProcessingThread"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(
                    messageBroadcastReceiver,
                    IntentFilter("ProcessingThread"),
                    Context.RECEIVER_EXPORTED
                )
            } else {
                registerReceiver(messageBroadcastReceiver, IntentFilter("ProcessingThread"))
            }

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(messageBroadcastReceiver)
    }

    override fun onDestroy() {
        val intent = Intent(applicationContext, Colocviu1_2Service::class.java)
        applicationContext.stopService(intent)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("sum", sum)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey("sum")) {
            sum = savedInstanceState.getInt("sum", -1)
        }

        if (sum != - 1)
            Toast.makeText(this, "sum is $sum ", Toast.LENGTH_LONG).show()
    }
}