package com.example.p3l.Adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l.Model.HistoryMember
import com.example.p3l.R

class HistoryAktivitasMemberAdapter(private var historys: List<HistoryMember>, context: Context): RecyclerView.Adapter<HistoryAktivitasMemberAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_history_aktivitas_member_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historys[position]
        holder.tvKodeBooking.text = "${data.KODE_BOOKING_KELAS} - ${data.NAMA_KELAS}"
        holder.tvTanggalBook.text = "Tanggal Kelas: ${data.TANGGAL_JADWAL_HARIAN}"
        holder.tvInstruktur.text = "Nama Instruktur: ${data.NAMA_INSTRUKTUR}"
        holder.tvTanggalMelakukan.text = "Tanggal Booking: ${data.TANGGAL_MELAKUKAN_BOOKING}"
        holder.tvStatusBooking.text = "${data.STATUS_PRESENSI_KELAS} - ${data.WAKTU_PRESENSI_KELAS}"
        if(holder.tvStatusBooking.text == "null - null"){
            holder.tvStatusBooking.text = "Belum dikonfirmasi"
        }

    }

    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKodeBooking: TextView
        var tvTanggalBook: TextView
        var tvInstruktur: TextView
        var tvTanggalMelakukan: TextView
        var tvStatusBooking: TextView
        var cvBook: CardView

        init {
            tvKodeBooking = view.findViewById(R.id.tv_kode_kelas)
            tvTanggalBook = view.findViewById(R.id.tv_tanggal_kelas)
            tvInstruktur = view.findViewById(R.id.tv_nama_instruktur)
            tvTanggalMelakukan = view.findViewById(R.id.tv_tanggal_melakukan)
            tvStatusBooking = view.findViewById(R.id.tv_status_konfirmasi)
            cvBook = view.findViewById(R.id.cv_book)
        }

    }
}