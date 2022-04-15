/*package to.boosty.cmit.loginactivity

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.net.URL
import java.net.URLEncoder


class GetData {

    fun async(login: String, pass: String, url_link: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                doUrl(url_link)
                }.onSuccess { result ->
                var data: String = URLEncoder.encode("login","UTF-8")+"="+URLEncoder.encode(login, "UTF-8")
                data +="&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass, "UTF-8")
                val conn = result.openConnection()
                conn.doOutput = true
                val writer = OutputStreamWriter(conn.getOutputStream())
                writer.write(data)
                writer.flush()
                val reader = BufferedReader(InputStreamReader(conn.getInputStream()))
                val sb = StringBuilder()
                var line = ""
                while (reader.readLine()!=null) {
                    Log.d("TAG", sb.toString())
                    line = reader.readLine()
                    sb.append(line)
                    break
                }
                Log.d("TAG", sb.toString())
            }.onFailure { t ->
                Log.d("TAG", "$t")
                t.printStackTrace()
            }

        }
    }

    private fun doUrl(url_link: String): URL = URL(url_link)
    fun processFinish(getData:String) {
        val intent: Intent = Intent(this, ViewActivity::class.java)
        intent.putExtra("full_name", getData)
        startActivity(intent)
    }
}*/