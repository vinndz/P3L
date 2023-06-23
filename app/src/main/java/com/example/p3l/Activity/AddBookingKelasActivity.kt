package com.example.p3l.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l.Adapter.AddBookingKelasAdapter
import com.example.p3l.Api.MemberApi
import com.example.p3l.Api.PegawaiApi
import com.example.p3l.Model.BookingClass
import com.example.p3l.Model.JadwalHarian
import com.example.p3l.R
import com.example.p3l.databinding.ActivityAddBookingKelasBinding
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddBookingKelasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookingKelasBinding
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookingKelasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getString("id", null)
        queue = Volley.newRequestQueue(this)

        allData()


//        binding.buttonCreate.setOnClickListener {
//            if (id != null) {
//                bookingClass(id)
//            }
//        }
    }

    private fun allData() {
        binding.srClass.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PegawaiApi.GETALLDATA_URL , Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<JadwalHarian>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = JadwalHarian(
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("KETERANGAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("HARI_JADWAL"),
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getDouble("TARIF"),
                    )
                    schedule.add(data)
                }
                var data_array: Array<JadwalHarian> = schedule.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : AddBookingKelasAdapter = AddBookingKelasAdapter(schedule,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_jadwalKelas)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srClass.isRefreshing = false

                if (!data_array.isEmpty()) {

                }else {

                }

            }, Response.ErrorListener { error ->
                binding.srClass.isRefreshing = true
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddBookingKelasActivity, errors.getString("message"),
                        Toast.LENGTH_LONG
                    ).show();
                } catch (e: java.lang.Exception) {
                    Toast.makeText(
                        this@AddBookingKelasActivity, e.message,
                        Toast.LENGTH_LONG
                    ).show();



                } catch (e: Exception){

                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                return headers
            }
        }
        queue!!.add(stringRequest)
    }




    fun bookingClass(id_member: String, id_kelas: Int, tanggal: String){
        val booking = BookingClass(
            id_member,
            id_kelas,
            tanggal,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, MemberApi.STOREBOOKKELAS, Response.Listener { response ->
                val gson = Gson()
                var booking_data = gson.fromJson(response, BookingClass::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("data")

                if(booking_data!= null) {

//                    val intent = Intent(this@AddBookingKelas, HomeActivity::class.java)
//                    sharedPreferences.edit()
//                        .putString("booking",null)
//                        .apply()
//                    startActivity(intent)
                    FancyToast.makeText(this,"Berhasil melakukan booking kelas",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
                    val intent = Intent(this@AddBookingKelasActivity, BookingKelasActivity::class.java)
                    startActivity(intent)
                }
                else {
                    FancyToast.makeText(this,"Tidak Berhasil melakukan booking kelass",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,false).show()
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(
                        this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false
                    ).show()
                }catch (e: java.lang.Exception) {
                    FancyToast.makeText(this, e.message, FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(booking)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}