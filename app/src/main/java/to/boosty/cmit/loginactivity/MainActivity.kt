package to.boosty.cmit.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.net.URLEncoder.*

class MainActivity : AppCompatActivity() {

    lateinit var etLogin: EditText
    lateinit var etPassword: EditText
    lateinit var btSubmit: Button
    private lateinit var login: String
    private lateinit var pass: String
    private lateinit var url_link: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPass)
        btSubmit = findViewById(R.id.btSubmit)

        url_link = "http://f0660148.xsph.ru/profile.php"

        btSubmit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    doUrl(url_link)
                }.onSuccess { result ->
                    withContext(Dispatchers.Main) {
                        val sb = StringBuilder()
                        val line = result.readLine()
                        sb.append(line)
                        MyIntent(sb.toString())
                    }
                }.onFailure { t ->
                    MyIntent("$t")
                }

            }

        }
    }

    private fun doUrl(url_link: String): BufferedReader {
        val t = URL(url_link)
        login = etLogin.text.toString()
        pass = etPassword.text.toString()
        val data: String =
            encode("login", "UTF-8") + "=" + encode(login, "UTF-8") + "&" + encode("password",
                "UTF-8") + "=" + encode(pass, "UTF-8")
        val conn = t.openConnection()
        conn.doOutput = true
        val writer = OutputStreamWriter(conn.getOutputStream())
        writer.write(data)
        writer.flush()
        return BufferedReader(InputStreamReader(conn.getInputStream()))
    }

    fun MyIntent(mes: String) {
        val intent = Intent(this, ViewActivity::class.java)
        intent.putExtra("full_name", mes)
        startActivity(intent)
    }
}