package com.example.invenio_talet

import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var title:EditText
    private lateinit var author:EditText
    private lateinit var date:EditText
    private lateinit var content:EditText
    private lateinit var  send: Button
    private lateinit var list:Button


    private var getTitle : String?=null
    private var getAuthor : String?=null
    private var getDate : String?=null
    private var getContent : String?=null
    private lateinit var textViewConnectionStatus: TextView
    private lateinit var connectivityManager: ConnectivityManager
    private var connectivityReceiver: ConnectivityReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = findViewById(R.id.titulo)
        author = findViewById(R.id.author)
        date = findViewById(R.id.date)
        content = findViewById(R.id.content)
        send = findViewById(R.id.send)
        textViewConnectionStatus = findViewById(R.id.textView2)
        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        list = findViewById(R.id.list)
        list.setOnClickListener {
            val intent = Intent(this, show_list::class.java)
            startActivity(intent)        }

        if (date != null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val fechaActual = sdf.format(Date())
            date.setText(fechaActual)
        }
        date.inputType=InputType.TYPE_NULL
        date.setOnClickListener {

            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            val picker = DatePickerDialog(
                this@MainActivity,
                androidx.appcompat.R.style.Base_Theme_AppCompat,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    date.text = Editable.Factory.getInstance().newEditable(selectedDate)
                },
                year, month, day
            )

            picker.show()
            picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
            picker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)

        }
        send.setOnClickListener {

            val getTitle = title.text.toString().trim()
            val getAuthor = author.text.toString().trim()
            val getDate = date.text.toString().trim()
            val getContent = content.text.toString().trim()
            val fieldsToValidate = listOf(
                Pair(title, "Title is required"),
                Pair(author, "Author is required"),
                Pair(date, "Date is required"),
                Pair(content, "Content is required")
            )

// Itera sobre los campos y realiza la validación
            for ((editText, errorMessage) in fieldsToValidate) {
                if (TextUtils.isEmpty(editText.text)) {
                    editText.error = errorMessage
                    editText.requestFocus()
                    return@setOnClickListener  // Termina la función si un campo está vacío
                }
            }
            title.setText("")
            author.setText("")
            content.setText("")
            enviarInformacion(getTitle,getAuthor,getDate,getContent)
        }

    }

    private fun enviarInformacion(title: String, author: String, date: String, content: String) {
            val url= utils_URL()
            val insertUrl = url.URL_INSERT

        val queue = Volley.newRequestQueue(this@MainActivity)
        val request: StringRequest =object : StringRequest(Method.POST, insertUrl, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                val sharedPreferences = getSharedPreferences("altas", Context.MODE_PRIVATE)

                if (obj.getBoolean("error")) {
                    Log.e("insertado?", "entra")
                    Toast.makeText(this@MainActivity, obj.getString("message"), Toast.LENGTH_SHORT).show()

                    val alta = obj.optString("alta", "") // Usar optString para manejar posibles valores no presentes
                    Log.e("regresa",alta);
                    Toast.makeText(this@MainActivity, obj.getString("alta"), Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@MainActivity, obj.getString("error"), Toast.LENGTH_SHORT).show()
                    val userJson: JSONObject = obj.getJSONObject("alta")

                    alta_guardado(
                        userJson.getString("title"),
                        userJson.getString("author"),
                        userJson.getString("date"),
                        userJson.getString("content"))

                    val title = userJson.getString("title")
                    val author = userJson.getString("author")
                    val date = userJson.getString("date")
                    val content = userJson.getString("content")
                    val altaGuardado = alta_guardado(title, author, date, content)

                    val savedAltaGuardadoListJson = sharedPreferences.getString("alta_guardado_list", null)
                    val gson = Gson()

                    val existingAltaGuardadoList: ArrayList<alta_guardado> = if (savedAltaGuardadoListJson != null) {
                        val type = object : TypeToken<ArrayList<alta_guardado>>() {}.type
                        gson.fromJson(savedAltaGuardadoListJson, type)
                    } else {
                        ArrayList()
                    }
                    existingAltaGuardadoList.add(altaGuardado)
                    val altaGuardadoListJson = gson.toJson(existingAltaGuardadoList)
                    val editor = sharedPreferences.edit()
                    editor.putString("alta_guardado_list", altaGuardadoListJson)
                    editor.apply()
                }
            } catch (e: JSONException) {
                // Manejar el error aquí, por ejemplo, mostrar un mensaje de error
                Log.e("JSON Error", "Error al analizar la respuesta JSON: ${e.message}")
                e.printStackTrace()
            }

        }, Response.ErrorListener { response ->
            val obj=  (response)

            Toast.makeText(
                this@MainActivity,
                "Error 01 =$response",
                Toast.LENGTH_SHORT
            ).show()
            Log.e("error", response.toString());

        }) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["title"] = title
                params["author"] = author
                params["date"] = date
                params["content"] = content
                return params
            }
        }
        queue.add(request)
    }
    override fun onResume() {
        super.onResume()
        connectivityReceiver = ConnectivityReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        if (connectivityReceiver != null) {
            unregisterReceiver(connectivityReceiver)
        }
    }

    inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable()) {
                textViewConnectionStatus.text = "Connection to internet "
                textViewConnectionStatus.setTextColor(Color.GREEN)
                send.isEnabled=true
            } else {
                textViewConnectionStatus.text = "Offline"
                textViewConnectionStatus.setTextColor(Color.RED)
                send.isEnabled=false
            }
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}