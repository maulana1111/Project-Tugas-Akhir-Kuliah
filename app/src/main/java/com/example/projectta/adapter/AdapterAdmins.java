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
import com.example.projectta.activity.DetailAdminActivity;
import com.example.projectta.activity.Detail_Donatur_activity;
import com.example.projectta.model.ModelAdmin;
import com.example.projectta.session.Session;

import java.util.List;

public class AdapterAdmins extends RecyclerView.Adapter<AdapterAdmins.MyViewHolder> {

    Context context;
    List<ModelAdmin> data;
    Session session;

    public AdapterAdmins(Context context, List<ModelAdmin> data) {
        this.context = context;
        this.data = data;
        session = new Session(context);
    }

    @Override
    public AdapterAdmins.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin, parent, false);
        return new AdapterAdmins.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( AdapterAdmins.MyViewHolder holder, int position) {
        holder.tvNama.setText(data.get(position).getNama());
        holder.tvLevel.setText(data.get(position).getLevel());

        Boolean access = data.get(position).getAccess();

        if(access == true)
        {
            holder.tvAccess.setText("Memiliki Akses");
            holder.ivAccess.setImageResource(R.drawable.ic_check);
        }else{
            holder.tvAccess.setText("Tidak Memiliki Akses");
            holder.ivAccess.setImageResource(R.drawable.ic_close);
        }
        int id = data.get(position).getId();

        if(session.getLevelAdmin().equals("pertama"))
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailAdminActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAccess;
        TextView tvNama, tvAccess, tvLevel;

        public MyViewHolder( View itemView) {
            super(itemView);

            ivAccess = itemView.findViewById(R.id.iv_access);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAccess = itemView.findViewById(R.id.tv_access);
            tvLevel = itemView.findViewById(R.id.tv_level);
        }
    }
}
