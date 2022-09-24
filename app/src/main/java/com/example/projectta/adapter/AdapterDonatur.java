package com.example.projectta.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectta.R;
import com.example.projectta.activity.Detail_Donatur_activity;
import com.example.projectta.model.DonaturModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class AdapterDonatur extends RecyclerView.Adapter<AdapterDonatur.MyViewHolder> {

    Context context;
    List<DonaturModel> data = new ArrayList<>();
    AlertDialog.Builder alert;
    EditText edKey;
    String txtKey;

    public AdapterDonatur(Context context, List<DonaturModel> data) {
        this.context = context;
        this.data = data;
        alert = new AlertDialog.Builder(context);
    }

    @Override
    public AdapterDonatur.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donatur, parent, false);
        return new AdapterDonatur.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterDonatur.MyViewHolder holder, int position) {

        int id = data.get(position).getId();
        String status = data.get(position).getStatus_show();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(status.equals("false")){
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View promp = inflater.inflate(R.layout.dialog_key, null);
                        alert.setView(promp);

                        edKey = promp.findViewById(R.id.ed_key);

                        alert.setCancelable(false);
                        alert.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                txtKey = edKey.getText().toString();
                                if(txtKey.trim().equals(""))
                                {
                                    edKey.setError("Tidak Boleh Kosong");
                                }else{
                                    Intent intent = new Intent(context, Detail_Donatur_activity.class);
                                    intent.putExtra("key", txtKey);
                                    intent.putExtra("id", id);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                }
                            }
                        });
                        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }else{
                        Intent intent = new Intent(context, Detail_Donatur_activity.class);
                        intent.putExtra("key", "1");
                        intent.putExtra("id", id);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            });

        String[] bulan = {"Januari", "Februari", "Maret", "Apri", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        rupiah.setDecimalFormatSymbols(formatRp);

        holder.tvNamaDonatur.setText(data.get(position).getPemilik_rekening());

        String getTanggal = data.get(position).getTanggal_donate();
        String[] getBulan = getTanggal.split("-");
        String countBulan = getBulan[1];
        String strBulan;
        if(countBulan.charAt(0) == '0')
        {
            strBulan = String.valueOf(countBulan.charAt(1));
        }else{
            strBulan = countBulan;
        }
        for(int j = 0; j< bulan.length; j++)
        {
            if((Integer.parseInt(strBulan)-1) == j)
            {
                holder.tvTanggal.setText(getBulan[0]+" "+bulan[j]+" "+getBulan[2]);
            }
        }

        String dataJum = data.get(position).getJumlah();
        int jum;
        try{
            jum = Integer.parseInt(dataJum);
            holder.tvJumlah.setText(rupiah.format(jum));
        }catch (NumberFormatException e) {
            holder.tvJumlah.setText(dataJum.substring(0,8));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaDonatur, tvJumlah, tvTanggal;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvNamaDonatur = itemView.findViewById(R.id.tv_nama_donatur);
            tvJumlah = itemView.findViewById(R.id.tv_jumlah);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
        }
    }
}
