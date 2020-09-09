package com.example.freemeal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var scanBarcode: Button
    private lateinit var responseTextView: TextView
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scanBarcode = findViewById(R.id.scanBarcode)
        responseTextView = findViewById(R.id.responseTextView)

        //sharedPreferences in private mode
        sharedPreferences = this.getSharedPreferences("ScanPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        //getting Date instance
        val time = Calendar.getInstance()

        /*checking if date has been saved in sharedPref before.
        empty "" is for first time (sharedPref default value) user opens the app, we don't expect
        a savedDate string on first app launch since the use has never scanned before*/
        val savedDate = sharedPreferences!!.getString("date", "")!!
        //if there's no savedDate
        if (savedDate == "") {
            //enable scan button
            scanBarcode.isEnabled = true
            //display barcode message
            responseTextView.text = ""
        } else {
            //else if there's a date saved in the sharedPref
            //get today's date and...
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            val date = dateFormat.format(time.time)
            //...compare to the date saved in sharedPref
            //if it's the same, user has scanned today
            //so disable the scan button and tell user to come back tomorrow
            if (date == savedDate) {
                scanBarcode.isEnabled = false
                responseTextView.setText(R.string.ErrorMessage)
            } else {
                //else, the dates don't match so, enable scan button and allow user to scan
                scanBarcode.isEnabled = true
                responseTextView.setText(R.string.scanText)
            }
        }

        //executes only if user hasn't scanned today (button is enabled)
        scanBarcode.setOnClickListener {
            //barcode
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()

            //on clicking the button, get today's date and save it in sharedPref
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            val date = dateFormat.format(time.time)
            editor.putString("date", date)
            editor.apply()
            //tell user to come back another day to scan
            responseTextView.setText(R.string.ErrorMessage)
            //disables the scan button
            scanBarcode.isEnabled = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Scan Failed", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Scanned: ", Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}