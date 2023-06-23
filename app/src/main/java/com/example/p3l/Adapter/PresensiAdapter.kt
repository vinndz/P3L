package com.example.p3l.Adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l.Activity.PresensiInstrukturActivity
import com.example.p3l.Model.PresensiInstruktur
import com.example.p3l.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PresensiAdapter (private var instructors: List<PresensiInstruktur>, context: Context): RecyclerView.Adapter<PresensiAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresensiAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_presensi_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresensiAdapter.ViewHolder, position: Int) {
        val data = instructors[position]
        holder.tvKelas.text =  data.NAMA_KELAS
        holder.tvInstruktur.text = "Nama Instruktur : " + data.NAMA_INSTRUKTUR
        holder.tvTanggal.text = "Tanggal " + data.TANGGAL_JADWAL_HARIAN
        holder.tvHari.text = "Hari :" + data.HARI_JADWAL
        holder.tvKeterangan.text = "Status : " + data.KETERANGAN
        holder.tvDurasiMulai.text = "Jam: " +data.JAM_MULAI + " - " +data.JAM_SELESAI


        holder.btnStart.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin update jam mulai kelas?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Iya"){ _, _ ->
                    if (context is PresensiInstrukturActivity){
                        context.store(data.ID_INSTRUKTUR,data.TANGGAL_JADWAL_HARIAN)
                    }
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return instructors.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeterangan: TextView
        var tvHari: TextView
        var tvTanggal: TextView
        var cvSchedule: CardView
        var btnStart: Button
        var tvDurasiMulai: TextView



        init {
            tvKelas = view.findViewById(R.id.text_kelas_instructor)
            tvInstruktur = view.findViewById(R.id.text_instruktur_instructor)
            tvKeterangan = view.findViewById(R.id.text_keterangan_kelas_instructor)
            tvHari = view.findViewById(R.id.tv_hari_instructor)
            tvTanggal = view.findViewById(R.id.text_tanggal_kelas_instructor)
            cvSchedule = view.findViewById(R.id.cv_presensi)
            btnStart = view.findViewById(R.id.btn_start)
            tvDurasiMulai = view.findViewById(R.id.tv_durasiMulai)
        }

    }
}