package com.example.projectta.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectta.R;
import com.example.projectta.activity.DetailFileActivity;
import com.example.projectta.activity.Detail_Donatur_activity;
import com.example.projectta.model.ModelFile;

import java.util.List;

public class AdapterFile extends RecyclerView.Adapter<AdapterFile.MyViewHolder> {

    private Context context;
    private List<ModelFile> data;

    public AdapterFile(Context context, List<ModelFile> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new AdapterFile.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTitle.setText(data.get(position).getFile_name_source());
        holder.tvTanggal.setText(data.get(position).getTanggal_upload());
        if(data.get(position).getStatus_file().equals("1"))
        {
            holder.ivKey.setImageResource(R.drawable.ic_security_on);
        }else{
            holder.ivKey.setImageResource(R.drawable.ic_security_off);
        }
        int id = data.get(position).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailFileActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTanggal;
        ImageView ivKey;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivKey = itemView.findViewById(R.id.iv_key);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
        }
    }
}
