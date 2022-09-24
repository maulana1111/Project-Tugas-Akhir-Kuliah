package com.example.projectta.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseAdmins;
import com.example.projectta.model.ModelAdmin;
import com.example.projectta.session.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAdminActivity extends AppCompatActivity {

    LinearLayout lyBack, btnAccept, btnReject, btnHapus;
    TextView tvNama, tvUsername, tvLevel, tvAccess;
    Session session;
    int paramId;
    List<ModelAdmin> data = new ArrayList<>();
    AlertDialog.Builder alert;
    EditText edKey;
    String txtKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_admin);
        checkConnectionInternet();
        session = new Session(DetailAdminActivity.this);
        alert = new AlertDialog.Builder(DetailAdminActivity.this);

        paramId = getIntent().getIntExtra("id", 0);

        lyBack = findViewById(R.id.btn_back);
        btnAccept = findViewById(R.id.btn_accept);
        btnReject = findViewById(R.id.btn_reject);
        btnHapus = findViewById(R.id.btn_hapus);

        tvNama = findViewById(R.id.tv_nama);
        tvUsername = findViewById(R.id.tv_username);
        tvLevel = findViewById(R.id.tv_level);
        tvAccess = findViewById(R.id.tv_access);

        lyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailAdminActivity.this, MainActivity.class);
                intent.putExtra("con_frag", 3);
                startActivity(intent);
                finish();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusData();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectData();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(DetailAdminActivity.this);
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
                            beriAkses();
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
            }
        });

        showData();

    }

    private void beriAkses()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseAdmins> beriAkses = ardData.beriAccess(session.getToken(), session.getKey(), txtKey, paramId);
        beriAkses.enqueue(new Callback<ResponseAdmins>() {
            @Override
            public void onResponse(Call<ResponseAdmins> call, Response<ResponseAdmins> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(DetailAdminActivity.this, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailAdminActivity.this, DetailAdminActivity.class);
                    intent.putExtra("id", paramId);
                    startActivity(intent);
                    finish();
                }else{
                    AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                    pesan.setTitle("Warning !!");
                    try {
                        pesan.setMessage("Error : "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pesan.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAdmins> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Error Server : "+t.getMessage());
                pesan.show();
            }
        });
    }

    private void rejectData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseAdmins> hapusAkses = ardData.hapusAccess(session.getToken(), session.getKey(), paramId);
        hapusAkses.enqueue(new Callback<ResponseAdmins>() {
            @Override
            public void onResponse(Call<ResponseAdmins> call, Response<ResponseAdmins> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(DetailAdminActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailAdminActivity.this, DetailAdminActivity.class);
                    intent.putExtra("id", paramId);
                    startActivity(intent);
                    finish();
                }else{
                    AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                    pesan.setTitle("Warning !!");
                    try {
                        pesan.setMessage("Error : "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pesan.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAdmins> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Error Server : "+t.getMessage());
                pesan.show();
            }
        });
    }

    private void hapusData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseAdmins> hapusDataAdmin = ardData.hapusData(session.getToken(), session.getKey(), paramId);
        hapusDataAdmin.enqueue(new Callback<ResponseAdmins>() {
            @Override
            public void onResponse(Call<ResponseAdmins> call, Response<ResponseAdmins> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(DetailAdminActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailAdminActivity.this, MainActivity.class);
                    intent.putExtra("con_frag", 3);
                    startActivity(intent);
                    finish();
                }else{
                    AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                    pesan.setTitle("Warning !!");
                    try {
                        pesan.setMessage("Error : "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pesan.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAdmins> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Error Server : "+t.getMessage());
                pesan.show();
            }
        });
    }

    private void showData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseAdmins> showData = ardData.getDetailAdmin(session.getToken(), session.getKey(), paramId);
        showData.enqueue(new Callback<ResponseAdmins>() {
            @Override
            public void onResponse(Call<ResponseAdmins> call, Response<ResponseAdmins> response) {
                if(response.isSuccessful())
                {
                    data = response.body().getData();
                    tvNama.setText(data.get(0).getNama());
                    tvUsername.setText(data.get(0).getUsername());
                    tvLevel.setText(data.get(0).getLevel());
                    tvAccess.setText(String.valueOf(data.get(0).getAccess()));
                    if(data.get(0).getAccess() == true)
                    {
                        btnAccept.setVisibility(View.GONE);
                        btnReject.setVisibility(View.VISIBLE);
                    }else{
                        btnAccept.setVisibility(View.VISIBLE);
                        btnReject.setVisibility(View.GONE);
                    }
                }else{
                    AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                    pesan.setTitle("Warning !!");
                    try {
                        pesan.setMessage("Error : "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pesan.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAdmins> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Error Server : "+t.getMessage());
                pesan.show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(DetailAdminActivity.this, MainActivity.class);
        intent.putExtra("con_frag", 3);
        startActivity(intent);
        finish();
    }

    public void checkConnectionInternet()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null == activeNetwork)
        {
            AlertDialog.Builder pesan = new AlertDialog.Builder(DetailAdminActivity.this);
            pesan.setTitle("Informasi");
            pesan.setMessage("Koneksi Internet Terputus");
            pesan.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            pesan.show();
        }
    }
}