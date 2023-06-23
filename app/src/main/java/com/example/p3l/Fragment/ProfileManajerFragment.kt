package com.example.p3l.Fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l.Activity.HomeActivity
import com.example.p3l.Activity.LoginActivity
import com.example.p3l.Api.PegawaiApi
import com.example.p3l.Model.Auth
import com.example.p3l.R
import com.example.p3l.databinding.FragmentProfileManajerBinding
import com.example.p3l.databinding.FragmentProfileMemberBinding
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ProfileManajerFragment : Fragment() {
    private var _binding: FragmentProfileManajerBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileManajerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        queue = Volley.newRequestQueue(activity)

        binding.btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout(){
        val auth = Auth(
            "",
            "")

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, PegawaiApi.LOGOUT_URL, Response.Listener { response -> val gson = Gson()
                var user_logout = gson.fromJson(response, Auth::class.java)


                if(user_logout != null) {

                    val intent = Intent(activity, LoginActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",-1)
                        .putString("id", null)
                        .putString("role",null)
                        .putString("token",null)
                        .apply()
                    FancyToast.makeText(context as Activity,"Berhasil Logout",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,false).show()
                    startActivity(intent)
                }
                else {
                    FancyToast.makeText(context as Activity,"Gagal Logout",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,false).show()
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    FancyToast.makeText(context as Activity, errors.getString("message"), FancyToast.LENGTH_LONG, FancyToast.INFO, false).show()
                }catch (e: Exception) {
                    FancyToast.makeText(context as Activity, e.message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(auth)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)

    }
}