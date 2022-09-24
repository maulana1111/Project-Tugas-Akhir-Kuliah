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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseDonatur;
import com.example.projectta.Response.ResponseDonaturDetail;
import com.example.projectta.Response.ResponseKey;
import com.example.projectta.model.DonaturModel;
import com.example.projectta.session.Session;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_Donatur_activity extends AppCompatActivity {

    LinearLayout lyBtnBack, lyBtnTerima, lyBtnTolak, lyBtnGantiKey, lyForBtn, lyDetail, lyForData;
    String txtKey;
    int paramId;
    TextView tvPemilik, tvNomorRekening, tvOrganisasi, tvJumlah, tvGmail, tvPesan, tvStatus, tvTanggal;
    Session session;
    List<DonaturModel> data = new ArrayList<>();
    private static final String LOG_TAG = "LogActivity";

    private float FACTOR = 1.0f;
    private ScaleGestureDetector scaleGestureDetector;

    ImageView ivImage, ivDetail;
    int lyCount = 1;
    String sessionToken, sessionKey;

    AlertDialog.Builder alert1;
    AlertDialog.Builder alert2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_donatur);
        checkConnectionInternet();
        session = new Session(Detail_Donatur_activity.this);
        sessionKey = session.getKey();
        sessionToken = session.getToken();
        paramId = getIntent().getIntExtra("id", 0);
        txtKey = getIntent().getStringExtra("key");

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        tvPemilik = findViewById(R.id.tv_nama_pemilik);
        tvNomorRekening = findViewById(R.id.tv_nomor_rekening);
        tvOrganisasi = findViewById(R.id.tv_organisasi);
        tvJumlah = findViewById(R.id.tv_jumlah);
        tvGmail = findViewById(R.id.tv_gmail);
        tvPesan = findViewById(R.id.tv_pesan);
        tvStatus = findViewById(R.id.tv_status);
        tvTanggal = findViewById(R.id.tv_tanggal);
        ivImage = findViewById(R.id.iv_bukti);
        ivDetail = findViewById(R.id.iv_detail);

        lyBtnBack = findViewById(R.id.ly_btn);
        lyBtnTerima = findViewById(R.id.ly_btn_terima);
        lyBtnTolak = findViewById(R.id.ly_btn_tolak);
        lyBtnGantiKey = findViewById(R.id.ly_btn_ganti_key);
        lyForBtn = findViewById(R.id.ly_for_btn);

        lyDetail = findViewById(R.id.ly_detail);
        lyForData = findViewById(R.id.ly_page_data);

        alert1 = new AlertDialog.Builder(Detail_Donatur_activity.this);
        alert2 = new AlertDialog.Builder(Detail_Donatur_activity.this);

        lyBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyCount--;
                if(lyCount == 0)
                {
                    Intent intent = new Intent(Detail_Donatur_activity.this, MainActivity.class);
                    intent.putExtra("con_frag", 1);
                    startActivity(intent);
                    finish();
                }else if(lyCount == 1)
                {
                    lyDetail.setVisibility(View.GONE);
                    lyForData.setVisibility(View.VISIBLE);
                }
            }
        });

        lyBtnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept_donasi();
            }
        });

        lyBtnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject_donasi();
            }
        });

        lyBtnGantiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptKey();
            }
        });
        showData();

    }

    private void promptKey()
    {
        LayoutInflater inflater = LayoutInflater.from(Detail_Donatur_activity.this);
        View promp = inflater.inflate(R.layout.dialog_key_lama, null);

        alert1.setView(promp);

        EditText edKey = promp.findViewById(R.id.ed_key_lama);

        alert1.setCancelable(false);
        alert1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String keyInput = edKey.getText().toString();
                if(keyInput.trim().equals(""))
                {
                    edKey.setError("Tidak Boleh Kosong");
                }else{
                    checkKey(keyInput);
                }
            }
        });
        alert1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog1 = alert1.create();
        alertDialog1.show();
    }

    private void checkKey(String edKeyInput)
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseKey> checkKey = ardData.CheckKey(sessionToken, sessionKey, edKeyInput, paramId);
        checkKey.enqueue(new Callback<ResponseKey>() {
            @Override
            public void onResponse(Call<ResponseKey> call, Response<ResponseKey> response) {
                if(response.isSuccessful())
                {
                    int status_code = response.body().getStatus_code();
                    String status_message = response.body().getStatus_message();
                    String status_key = response.body().getStatus_key();

                    if(status_key.equals("true"))
                    {
                        LayoutInflater inflater = LayoutInflater.from(Detail_Donatur_activity.this);
                        View promp = inflater.inflate(R.layout.dialog_key_baru, null);
                        alert2.setView(promp);

                        EditText edKey1 = promp.findViewById(R.id.ed_key_baru);

                        alert2.setCancelable(false);
                        alert2.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String keyInput = edKey1.getText().toString();
                                if(keyInput.trim().equals(""))
                                {
                                    edKey1.setError("Tidak Boleh Kosong");
                                }else{
                                    ChangeKey(edKeyInput, keyInput);
                                }
                            }
                        });
                        alert2.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                        AlertDialog alertDialog = alert2.create();
                        alertDialog.show();
                    }else{
                        AlertDialog.Builder pesann = new AlertDialog.Builder(Detail_Donatur_activity.this);
                        pesann.setTitle("Warning !!");
                        pesann.setMessage("Key lama yang dimasukan salah");
                        pesann.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Detail_Donatur_activity.this, Detail_Donatur_activity.class);
                                intent.putExtra("key", txtKey);
                                intent.putExtra("id", paramId);
                                startActivity(intent);
                                finish();
                            }
                        });
                        pesann.show();
                    }
                }else{
                    try {
                        Log.e(LOG_TAG, "error change Keys : "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
            }

            @Override
            public void onFailure(Call<ResponseKey> call, Throwable t) {
                Log.e(LOG_TAG, "error check Key : "+t.getMessage());
                Toast.makeText(Detail_Donatur_activity.this, "Tidak Dapat Menghubungi Server = "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ChangeKey(String keyLama, String keyBaru)
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseDonaturDetail> changeKey = ardData.ChangeKey(sessionToken, sessionKey, keyLama, keyBaru, paramId);
        changeKey.enqueue(new Callback<ResponseDonaturDetail>() {
            @Override
            public void onResponse(Call<ResponseDonaturDetail> call, Response<ResponseDonaturDetail> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(Detail_Donatur_activity.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Detail_Donatur_activity.this, MainActivity.class);
                    intent.putExtra("con_frag", 1);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Detail_Donatur_activity.this, "Something Error : "+response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e(LOG_TAG, "error change Keys : "+response.errorBody().string());
                        Intent intent = new Intent(Detail_Donatur_activity.this, MainActivity.class);
                        intent.putExtra("con_frag", 1);
                        startActivity(intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDonaturDetail> call, Throwable t) {
                Log.e(LOG_TAG, "error change Key : "+t.getMessage());
                Toast.makeText(Detail_Donatur_activity.this, "Tidak Dapat Menghubungi Server = "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  accept_donasi()
    {  
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseDonaturDetail> changeAction = ardData.ActionDataDonasi(sessionToken, sessionKey, "1", paramId);
        changeAction.enqueue(new Callback<ResponseDonaturDetail>() {
            @Override
            public void onResponse(Call<ResponseDonaturDetail> call, Response<ResponseDonaturDetail> response) {
                Intent intent = new Intent(Detail_Donatur_activity.this, MainActivity.class);
                intent.putExtra("con_frag", 1);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseDonaturDetail> call, Throwable t) {
                Log.e(LOG_TAG, "error failure accept action : "+t.getMessage());
                Toast.makeText(Detail_Donatur_activity.this, "Tidak Dapat Menghubungi Server = "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  reject_donasi()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseDonaturDetail> changeAction = ardData.ActionDataDonasi(sessionToken, sessionKey, "2", paramId);
        changeAction.enqueue(new Callback<ResponseDonaturDetail>() {
            @Override
            public void onResponse(Call<ResponseDonaturDetail> call, Response<ResponseDonaturDetail> response) {
                Toast.makeText(Detail_Donatur_activity.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Detail_Donatur_activity.this, MainActivity.class);
                intent.putExtra("con_frag", 1);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseDonaturDetail> call, Throwable t) {
                Log.e(LOG_TAG, "error failure accept action : "+t.getMessage());
                Toast.makeText(Detail_Donatur_activity.this, "Tidak Dapat Menghubungi Server = "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseDonaturDetail> showDataDetail = ardData.getDataDonaturDetail(sessionToken, sessionKey, txtKey, paramId);
        showDataDetail.enqueue(new Callback<ResponseDonaturDetail>() {
            @Override
            public void onResponse(Call<ResponseDonaturDetail> call, Response<ResponseDonaturDetail> response) {

                if(response.isSuccessful())
                {
                    String[] bulan = {"Januari", "Februari", "Maret", "Apri", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

                    int param = response.body().getStatus_code();
                    String ext_time = response.body().getExecution_time();
                    if(ext_time != null)
                    {
                        Toast.makeText(Detail_Donatur_activity.this, "Hasil Eksekusi adalah = "+ext_time+" Sec", Toast.LENGTH_SHORT).show();
                    }
                    data = response.body().getData();
                    tvPemilik.setText(data.get(0).getPemilik_rekening());
                    tvNomorRekening.setText(data.get(0).getNo_rekening());
                    tvOrganisasi.setText(data.get(0).getOrganisasi());

                    tvGmail.setText(data.get(0).getGmail());
                    tvPesan.setText(data.get(0).getPesan());
                    tvStatus.setText(data.get(0).getStatus());
                    String getTanggal = data.get(0).getTanggal_donate();
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
                            tvTanggal.setText(getBulan[0]+" "+bulan[j]+" "+getBulan[2]);
                        }
                    }

                    if(data.get(0).getStatus_enkrip().equals("terlihat"))
                    {
                        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');

                        rupiah.setDecimalFormatSymbols(formatRp);
                        tvJumlah.setText(rupiah.format(Integer.parseInt(data.get(0).getJumlah())));

                        Glide.with(getApplicationContext()).load("http://192.168.18.45/AdminKrcV2/uploads/donatur/"+data.get(0).getBukti_transfer()).into(ivImage);
                        ivImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lyCount++;
                                lyForData.setVisibility(View.GONE);
                                lyDetail.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext()).load("http://192.168.18.45/AdminKrcV2/uploads/donatur/"+data.get(0).getBukti_transfer()).into(ivDetail);
                            }
                        });

                        lyForBtn.setVisibility(View.VISIBLE);
                    }else{
                        ivImage.setBackgroundResource(R.drawable.ic_no_photo);
                        tvJumlah.setText(data.get(0).getJumlah());
                        AlertDialog.Builder pesan = new AlertDialog.Builder(Detail_Donatur_activity.this);
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("Key is Wrong");
                        pesan.show();
                        lyForBtn.setVisibility(View.GONE);
                    }

                    if(!data.get(0).getStatus_show().equals("true"))
                    {
                        lyBtnGantiKey.setVisibility(View.VISIBLE);
                    }else{
                        lyBtnGantiKey.setVisibility(View.GONE);
                    }

                    if(!data.get(0).getStatus().equals("proses"))
                    {
                        lyBtnTerima.setVisibility(View.GONE);
                        lyBtnTolak.setVisibility(View.GONE);
                    }else{
                        lyBtnTerima.setVisibility(View.VISIBLE);
                        lyBtnTolak.setVisibility(View.VISIBLE);
                    }
                }else{
                    try {
                        Log.e(LOG_TAG, "Error :"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDonaturDetail> call, Throwable t) {
                Log.e(LOG_TAG, "error failure : "+t.getMessage());
                Toast.makeText(Detail_Donatur_activity.this, "Tidak Dapat Menghubungi Server = "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        scaleGestureDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
        return scaleGestureDetector.onTouchEvent(event);
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            FACTOR *= detector.getScaleFactor();
            FACTOR = Math.max(0.1f, Math.min(FACTOR, 10.f));
            ivDetail.setScaleX(FACTOR);
            ivDetail.setScaleY(FACTOR);
            return true;
        }
    }

    @Override
    public void onBackPressed()
    {
        lyCount--;
        if(lyCount == 0)
        {
            Intent intent = new Intent(Detail_Donatur_activity.this, MainActivity.class);
            intent.putExtra("con_frag", 1);
            startActivity(intent);
            finish();
        }else if(lyCount == 1)
        {
            lyDetail.setVisibility(View.GONE);
            lyForData.setVisibility(View.VISIBLE);
        }
    }

    public void checkConnectionInternet()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null == activeNetwork)
        {
            AlertDialog.Builder pesan = new AlertDialog.Builder(Detail_Donatur_activity.this);
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