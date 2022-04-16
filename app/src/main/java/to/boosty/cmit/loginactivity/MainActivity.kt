package to.boosty.cmit.loginactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvFN: TextView
    private lateinit var shPref: SharedPreferences
    val MY_PREF = "LOGINPASSAPP_PREFERENCES_FILE"
    var FULLUSERNAME = ""
    private val code = 0
    //val fullUserName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TAG", "onCreate start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvFN = findViewById(R.id.tvViewFN)

        val fullUserName = loadUserFN()
        if (fullUserName == null) getUserFN()
        else tvFN.text = "Hi, $fullUserName"
    }

    fun loadUserFN(): String? {
        Log.d("TAG", "loadUserFN start")
        shPref = getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE)
        val fullUserName = shPref.getString(FULLUSERNAME, "")
        return if (fullUserName?.isEmpty() == true) null
        else fullUserName
    }

    fun getUserFN() {
        Log.d("TAG", "getUserFN start")
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivityResult start")
        if (requestCode == code) {
            if (resultCode == RESULT_OK) {
                val fullUserName = data?.getStringExtra(FULLUSERNAME)
                saveFUN(fullUserName!!)
                tvFN.text = ("Hi, $fullUserName")
            } else {
                Toast.makeText(this, "User not found!", LENGTH_SHORT).show()
                getUserFN()
            }
        }
    }

    private fun saveFUN(data: String) {
        Log.d("TAG", "saveFUN start")
        shPref = getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE)
        val edit: SharedPreferences.Editor = shPref.edit()
        edit.putString(FULLUSERNAME, data)
        edit.apply()
    }
}