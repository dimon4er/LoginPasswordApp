package to.boosty.cmit.loginactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class ViewActivity : Activity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val tvFN: TextView = findViewById(R.id.tvViewFN)
        val intent = intent
        val fullname = intent.getStringExtra("full_name")
        tvFN.text = "Your name is: $fullname"
    }
}