package com.example.p3l.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l.Adapter.BookingKelasAdapter
import com.example.p3l.Adapter.PresensiBookingKelasAdapter
import com.example.p3l.Api.PresensiBookingKelasApi
import com.example.p3l.Model.HistoryBookingKelas
import com.example.p3l.R
import com.example.p3l.databinding.ActivityPresensiBookingKelasBinding
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class PresensiBookingKelasActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityPresensiBookingKelasBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresensiBookingKelasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id",0)
        val tanggal = sharedPreferences.getString("tanggal_jadwal_harian",null)
        queue = Volley.newRequestQueue(this)

        binding.srPresensiMember.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{
            if (tanggal != null) {
                allData(tanggal)
            }
        })
        if (tanggal != null) {
            allData(tanggal)
        }
    }
    private fun allData(id: String) {
        binding.srPresensiMember.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PresensiBookingKelasApi.GETPRESENSIMEMBER + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var history = arrayListOf<HistoryBookingKelas>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = HistoryBookingKelas(
                        jo.getJSONArray("data").getJSONObject(i).getString("KODE_BOOKING_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_MEMBER"),
                        jo.getJSONArray("data").getJSONObject(i).getString("WAKTU_PRESENSI_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_PRESENSI_KELAS")
                    )
                    history.add(data)
                }
                var data_array: Array<HistoryBookingKelas> = history.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : PresensiBookingKelasAdapter= PresensiBookingKelasAdapter(history,this)
                val rvPermission : RecyclerView = findViewById(R.id.list_presensi_member)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srPresensiMember.isRefreshing = false

                if (!data_array.isEmpty()) {
                  Toast.makeText(this@PresensiBookingKelasActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this, "Notification Display!",
                        "Succesfully get data",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }else {
                    MotionToast.darkToast(
                        this, "Notification Display!",
                        "Data not found",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }

            }, Response.ErrorListener { error ->
                binding.srPresensiMember.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(this@JanjiTemuActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        errors.getString("message"),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srPresensiMember.isRefreshing = false
                } catch (e: Exception){
//                    Toast.makeText(this@JanjiTemuActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srPresensiMember.isRefreshing = false
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



    fun update(kode_booking: String, status: String){
        val booking = HistoryBookingKelas(
            kode_booking,
            null,
            null,
            null,
            null,
            status,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, PresensiBookingKelasApi.UPDATE, Response.Listener { response ->
                val gson = Gson()
                var booking_data = gson.fromJson(response, HistoryBookingKelas::class.java)

                var resJO = JSONObject(response.toString())
                val userobj = resJO.getJSONObject("data")

                if(booking_data!= null) {
                    MotionToast.darkColorToast(this,"Notification Booking!",
                        resJO.getString("message"),
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@PresensiBookingKelasActivity, JadwalPresensiInstrukturActivity::class.java)
                    sharedPreferences.edit()
                        .putString("tanggal_jadwal_harian",null)
                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Booking!",
                        "Failed Confirm Booking Class",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,"Notification Booking!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@PresensiBookingKelasActivity, e.message,
                        Toast.LENGTH_LONG).show();
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(booking)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}