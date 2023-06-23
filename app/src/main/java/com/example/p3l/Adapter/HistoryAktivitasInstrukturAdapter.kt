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
import com.example.p3l.Model.HistoryInstruktur
import com.example.p3l.R

class HistoryAktivitasInstrukturAdapter(private var historys: List<HistoryInstruktur>, context: Context): RecyclerView.Adapter<HistoryAktivitasInstrukturAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_history_aktivitas_instruktur_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historys[position]
        holder.tvKodeBooking.text = "${data.NAMA_KELAS}"
        holder.tvInstruktur.text = "Nama Instruktur: ${data.NAMA_INSTRUKTUR}"
        holder.tvTarif.text = "Tarif:  ${data.TARIF}"
        holder.tvTanggal.text = "Tanggal Kelas: ${data.TANGGAL_JADWAL}"
        holder.tvHari.text = "Hari: ${data.HARI_JADWAL}"
        holder.tvWaktu.text = "Waktu Jadwal Umum: ${data.SESI_JADWAL}"
        holder.tvJam.text = "Jam: ${data.JAM_MULAI} - ${data.JAM_SELESAI}"
        if(holder.tvJam.text == "Jam: null - null"){
            holder.tvJam.text = "Kelas belum dimulai"
        }

    }

    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKodeBooking: TextView
        var tvTanggal: TextView
        var tvInstruktur: TextView
        var tvTarif: TextView
        var tvHari: TextView
        var tvWaktu: TextView
        var tvJam: TextView
        var cvBook: CardView

        init {
            tvKodeBooking = view.findViewById(R.id.tv_kode_kelas)
            tvTanggal = view.findViewById(R.id.tv_tanggal_kelas)
            tvInstruktur = view.findViewById(R.id.tv_nama_instruktur)
            tvTarif  = view.findViewById(R.id.tv_tarif)
            tvHari = view.findViewById(R.id.tv_hari)
            tvWaktu = view.findViewById(R.id.tv_waktu)
            tvJam = view.findViewById(R.id.tv_jam)
            cvBook = view.findViewById(R.id.cv_book)
        }

    }


}