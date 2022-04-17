package to.boosty.cmit.loginactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import to.boosty.cmit.loginactivity.utils.PhotosUtils
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvFN: TextView
    private lateinit var shPref: SharedPreferences
    val MY_PREF = "LOGINPASSAPP_PREFERENCES_FILE"
    var FULLUSERNAME = ""
    private lateinit var btSendMessage: Button
    private lateinit var btGetContact: Button
    private lateinit var intentGetContact: Intent
    private lateinit var intentGetPhoto: Intent
    private lateinit var ibPhoto: ImageButton
    private lateinit var ivPhoto: ImageView
    private lateinit var selfPhotoFile: File

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        //Log.d("TAG", "onCreate start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvFN = findViewById(R.id.tvViewFN)
        btSendMessage = findViewById(R.id.btSendMsg)

        val fullUserName = loadUserFN()
        if (fullUserName == null) //packageManager.MATCH_DEFAULT_ONLY))
        {
            getUserFN()
        } else {
            tvFN.text = "Hi, $fullUserName"
            btSendMessage.setOnClickListener (this)

            intentGetContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            val packageManager: PackageManager = this.getPackageManager()
            if (packageManager.resolveActivity(intentGetContact, 0) == null) { //packageManager.MATCH_DEFAULT_ONLY))
                btGetContact.isEnabled = false
            } else {
                btGetContact = findViewById(R.id.btGetContact)
                btGetContact.setOnClickListener(this)
            }
            ivPhoto = findViewById(R.id.ivMyPhoto)
            ibPhoto = findViewById(R.id.ibPhoto)
            intentGetPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFileName = getPhotoFileName ()
            selfPhotoFile = getPhotoFile (photoFileName)
            val canTakePhoto = intentGetPhoto.resolveActivity(packageManager) != null
            ibPhoto.isEnabled = canTakePhoto
            if (canTakePhoto) {
                val uri = FileProvider.getUriForFile(this, "to.boosty.cmit.loginactivity.fileprovider", selfPhotoFile)
                intentGetPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            ibPhoto.setOnClickListener(this)
        }
    }

    private fun getPhotoFileName(): String {
        val date = Date()
        return "IMG 20.20.20.jpg"
    }

    private fun getPhotoFile(fileName: String): File {
        val filesDir = this.filesDir
        //TODO: insert check file exists
        return File(filesDir, fileName)
    }

    override fun onClick (view: View) {
        if (view.id == btSendMessage.id){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hello, Dear dear!")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Report")
            val intentChooser = Intent.createChooser(intent, "SendMsg")
            startActivity(intentChooser)
        } else if  (view.id == btGetContact.id){
            getCommonIntentResult.launch(intentGetContact)
        } else if (view.id == ibPhoto.id) {
            Log.d("TAG", "Photo start")
            getPhotoResult.launch(intentGetPhoto)
        }
    }


    fun loadUserFN(): String? {
        //Log.d("TAG", "loadUserFN start")
        shPref = getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE)
        val fullUserName = shPref.getString(FULLUSERNAME, "")
        return if (fullUserName?.isEmpty() == true) null
        else fullUserName
    }

    fun getUserFN() {
        //Log.d("TAG", "getUserFN start")
        val intent = Intent(this, LoginActivity::class.java)
        getResult.launch(intent)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val fullUserName = it.data?.getStringExtra(FULLUSERNAME)
            saveFUN(fullUserName!!)
            tvFN.text = ("Hi, $fullUserName")
        }
    }

    private fun saveFUN(data: String) {
        shPref = getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE)
        val edit: SharedPreferences.Editor = shPref.edit()
        edit.putString(FULLUSERNAME, data)
        edit.apply()
    }

    private val getCommonIntentResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            if (it.data != null)  {
                val contactUri = it.data!!.getData()
                val queryFields = arrayOf<String> (ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = this.contentResolver.query(contactUri!!, queryFields, null, null, null)
                try {
                    if (cursor?.count == 0) return@registerForActivityResult
                    cursor?.moveToFirst()
                    val name = cursor?.getString(0)
                    //Log.d("TAG", "onActivityResult: name = $name")
                } finally {
                   cursor?.close()
                }

            }
        }
    }

    private fun updateSelfPhotoImageView (photoFile: File) {
        if (photoFile == null || !photoFile.exists()) {
            ivPhoto.setImageDrawable(null)
            Log.d("TAG", "ERROR File exists")
        } else {
            val size = Point()
            Log.d("TAG", "updateSelfPhotoImageView Start")
            this.windowManager.defaultDisplay.getSize(size) //TODO: deprecated
            val bitmap = PhotosUtils().getScaledBitmap(photoFile.path, size.x, size.y)
            ivPhoto.setImageBitmap(bitmap)
        }
    }

    private val getPhotoResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            Log.d("TAG", "getPhotoResult OK")
            updateSelfPhotoImageView(selfPhotoFile)
        } else {
            Log.d("TAG", "getPhotoResult ERROR")
        }
    }
}