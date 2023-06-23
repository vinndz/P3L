package com.example.p3l.Adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l.Activity.BookingGymActivity
import com.example.p3l.Model.BookingGym
import com.example.p3l.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookingGymAdapter(private var BookingGymList: List<BookingGym>, context: Context) :
    RecyclerView.Adapter<BookingGymAdapter.ViewHolder>() {

    private var filteredBookingGymList: MutableList<BookingGym>
    private val context: Context

    init {
        filteredBookingGymList = ArrayList(BookingGymList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_booking_gym_adapter, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredBookingGymList.size
    }

    fun setBookingGymList(BookingGymList: Array<BookingGym>) {
        this.BookingGymList = BookingGymList.toList()
        filteredBookingGymList = BookingGymList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = BookingGymList[position]

        holder.tvKodeBookingGym.text = data.KODE_BOOKING_GYM
        if (holder.tvStatusBookingGym.text == "Status: null - null") {
            holder.tvStatusBookingGym.text = "Belum dikonfirmasi"
        }
        holder.tvTanggalMelakukanGym.text = "Tanggal Booking: ${data.TANGGAL_MELAKUKAN_BOOK_GYM}"
        holder.tvSlotWaktu.text = "Jam : ${data.SLOT_WAKTU_GYM}"
        holder.tvStatusBookingGym.text =
            "Status: ${data.STATUS_PRESENSI_GYM} - ${data.WAKTU_PRESENSI_GYM}"

        holder.tvTanggalBookGym.text = "Tanggal Gym: ${data.TANGGAL_BOOK_GYM}"


        holder.iconDel.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin membatalkan booking gym ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Iya") { _, _ ->
                    if (context is BookingGymActivity) {
                        context.cancelBookingGym(data.KODE_BOOKING_GYM)
                    }
                }
                .show()
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvKodeBookingGym: TextView
        var tvTanggalBookGym: TextView
        var tvTanggalMelakukanGym: TextView
        var tvSlotWaktu: TextView
        var tvStatusBookingGym: TextView
        var iconDel: ImageButton
        var cvBookGym: CardView

        init {
            tvKodeBookingGym = view.findViewById(R.id.tv_kode_gym)
            tvTanggalBookGym = view.findViewById(R.id.tv_tanggal_gym)
            tvTanggalMelakukanGym = view.findViewById(R.id.tv_tanggal_dibooking_gym)
            tvSlotWaktu = view.findViewById(R.id.tv_slot_waktu)
            tvStatusBookingGym = view.findViewById(R.id.tv_status_konfirmasi_gym)
            iconDel = view.findViewById(R.id.btn_delete)
            cvBookGym = view.findViewById(R.id.cv_book)
        }
    }
}