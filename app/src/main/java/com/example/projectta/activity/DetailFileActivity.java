package com.example.projectta.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseFIle;
import com.example.projectta.model.ModelFile;
import com.example.projectta.session.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFileActivity extends AppCompatActivity {

    int paramId;
    Session session;

    TextView tvNamaFile, tvSize, tvWaktuUpload, tvWaktuUpdate, tvStatus, tvKeterangan;
    String sessionToken, sessionKey, txtKeyEnkrip, txtKeyDekrip, path, namaFile;
    List<ModelFile> data = new ArrayList<>();
    LinearLayout btnEnkrip, btnDekrip, btnDownload, lyBtn;
    AlertDialog.Builder alertEnkrip, alertDekrip;
    EditText edKeyEnkrip, edKeyDekrip;
    private final int REQUEST_READ_PHONE_STATE=1;
    private static final int PERMISSION_CODE = 1000;

    private static final String LOG_TAG = "LogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_file);
        checkConnectionInternet();
        session = new Session(DetailFileActivity.this);
        sessionKey = session.getKey();
        sessionToken = session.getToken();
        paramId = getIntent().getIntExtra("id", 0);

        alertEnkrip = new AlertDialog.Builder(DetailFileActivity.this);
        alertDekrip = new AlertDialog.Builder(DetailFileActivity.this);
        tvNamaFile = findViewById(R.id.tv_nama_file);
        tvSize = findViewById(R.id.tv_size);
        tvWaktuUpload = findViewById(R.id.tv_waktu_upload);
        tvWaktuUpdate = findViewById(R.id.tv_waktu_update);
        tvStatus = findViewById(R.id.tv_status);
        tvKeterangan = findViewById(R.id.tv_keterangan);

        btnEnkrip = findViewById(R.id.ly_btn_enkrip);
        btnDekrip = findViewById(R.id.ly_btn_dekrip);
        btnDownload = findViewById(R.id.ly_btn_download);
        lyBtn = findViewById(R.id.ly_btn);

        lyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailFileActivity.this, MainActivity.class);
                intent.putExtra("con_frag", 2);
                startActivity(intent);
                finish();
            }
        });

        btnEnkrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(DetailFileActivity.this);
                View promp = inflater.inflate(R.layout.dialog_key_enkrip, null);
                alertEnkrip.setView(promp);

                edKeyEnkrip = promp.findViewById(R.id.ed_key);

                alertEnkrip.setCancelable(false);
                alertEnkrip.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtKeyEnkrip = edKeyEnkrip.getText().toString();
                        if(txtKeyEnkrip.trim().equals(""))
                        {
                            edKeyEnkrip.setError("Tidak Boleh Kosong");
                        }else{
                            enkripData();
                        }
                    }
                });
                alertEnkrip.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                AlertDialog alertDialog = alertEnkrip.create();
                alertDialog.show();
            }
        });

        btnDekrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(DetailFileActivity.this);
                View promp = inflater.inflate(R.layout.dialog_key_dekrip, null);
                alertDekrip.setView(promp);

                edKeyDekrip = promp.findViewById(R.id.ed_key);

                alertDekrip.setCancelable(false);
                alertDekrip.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtKeyDekrip = edKeyDekrip.getText().toString();
                        if(txtKeyDekrip.trim().equals(""))
                        {
                            edKeyDekrip.setError("Tidak Boleh Kosong");
                        }else{
                            dekripData();
                        }
                    }
                });
                alertDekrip.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                AlertDialog alertDialog = alertDekrip.create();
                alertDialog.show();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }else{
                        downloadFile();
                    }
                }else{
                    downloadFile();
                }
            }
        });


        showData();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    downloadFile();
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onBackPressed()
    {
            Intent intent = new Intent(DetailFileActivity.this, MainActivity.class);
            intent.putExtra("con_frag", 2);
            startActivity(intent);
            finish();
    }

    private void downloadFile()
    {
        Uri uri = Uri.parse(path);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(namaFile);
        request.setDescription("Downloading .....");
        String cookie = CookieManager.getInstance().getCookie(path);
        request.addRequestHeader("Cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, namaFile);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        Toast.makeText(this, "Download Starting", Toast.LENGTH_SHORT).show();
    }

    private void enkripData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseFIle> enkripData = ardData.enkripData(sessionToken, sessionKey, txtKeyEnkrip, paramId);
        enkripData.enqueue(new Callback<ResponseFIle>() {
            @Override
            public void onResponse(Call<ResponseFIle> call, Response<ResponseFIle> response) {
                if(response.isSuccessful())
                {
                    int param = response.body().getStatus_code();
                    if(param != 200)
                    {
                        AlertDialog.Builder pesan = new AlertDialog.Builder(DetailFileActivity.this);
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("Error code : "+param+", Message : "+response.body().getStatus_message());
                        pesan.show();
                    }else{
                        Toast.makeText(DetailFileActivity.this, "Data Berhasil Diupdate, dengan hasil eksekusi = "+response.body().getExecution_time()+" Sec", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailFileActivity.this, DetailFileActivity.class);
                        intent.putExtra("id", paramId);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    try {
                        Log.e(LOG_TAG, "Error Detail File :"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFIle> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(DetailFileActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Something Wrong : "+t.getMessage());
                pesan.show();
            }
        });
    }

    private void dekripData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseFIle> dekripData = ardData.dekripData(sessionToken, sessionKey, txtKeyDekrip, paramId);
        dekripData.enqueue(new Callback<ResponseFIle>() {
            @Override
            public void onResponse(Call<ResponseFIle> call, Response<ResponseFIle> response) {
                if(response.isSuccessful())
                {
                    int param = response.body().getStatus_code();
                    if(param != 200)
                    {
                        AlertDialog.Builder pesan = new AlertDialog.Builder(DetailFileActivity.this);
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("Error code : "+param+", Message : "+response.body().getStatus_message());
                        pesan.show();
                    }else{
                        Toast.makeText(DetailFileActivity.this, "Data Berhasil Diupdate, dengan hasil eksekusi = "+response.body().getExecution_time()+" Sec", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailFileActivity.this, DetailFileActivity.class);
                        intent.putExtra("id", paramId);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    try {
                        Log.e(LOG_TAG, "Error Detail File :"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFIle> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(DetailFileActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Something Wrong : "+t.getMessage());
                pesan.show();
            }
        });
    }

    private void showData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseFIle> showData = ardData.GetFilebyId(sessionToken, sessionKey, paramId);
        showData.enqueue(new Callback<ResponseFIle>() {
            @Override
            public void onResponse(Call<ResponseFIle> call, Response<ResponseFIle> response) {
                if(response.isSuccessful())
                {
                    int param = response.body().getStatus_code();
                    if(param != 200)
                    {
                        AlertDialog.Builder pesan = new AlertDialog.Builder(DetailFileActivity.this);
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("Something Wrong : "+response.body().getStatus_message());
                        pesan.show();
                    }else{
                        data = response.body().getData();

                        String size = data.get(0).getFile_size();

                        String sts = data.get(0).getStatus_file();

                        path = data.get(0).getFile_path();
                        namaFile = data.get(0).getFile_name_source();

                        tvNamaFile.setText(namaFile);
                        tvSize.setText(size+" KB");
                        tvWaktuUpload.setText(data.get(0).getTanggal_upload());
                        tvWaktuUpdate.setText(data.get(0).getTanggal_update());
                        tvKeterangan.setText(data.get(0).getKeterangan());

                        if(sts.equals("1"))
                        {
                            btnDekrip.setVisibility(View.VISIBLE);
                            btnEnkrip.setVisibility(View.GONE);
                            tvStatus.setText("Terenkripsi");
                        }else{
                            btnDekrip.setVisibility(View.GONE);
                            btnEnkrip.setVisibility(View.VISIBLE);
                            tvStatus.setText("Terdekripsi");
                        }

                    }
                }else{
                    try {
                        Log.e(LOG_TAG, "Error Detail File :"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFIle> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(DetailFileActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Something Wrong : "+t.getMessage());
                pesan.show();
            }
        });
    }

    public void checkConnectionInternet()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null == activeNetwork)
        {
            AlertDialog.Builder pesan = new AlertDialog.Builder(DetailFileActivity.this);
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