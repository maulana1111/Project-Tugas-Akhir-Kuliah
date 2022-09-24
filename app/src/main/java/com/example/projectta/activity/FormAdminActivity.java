package com.example.projectta.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseAdmins;
import com.example.projectta.session.Session;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormAdminActivity extends AppCompatActivity {

    EditText edNama, edUsername, edPassword, edKey;
    RadioGroup rbGroupLevel, rbGroupAccess;
    RadioButton rbButtonLevel, rbButtonAccess;
    TextView tvKey, btn;
    LinearLayout lyBack;
    Session session;
    int selectedRadioButtonIdOpsiLevel, selectedRadioButtonIdOpsiAccess;

    String txtNama, txtUsername, txtPassword, txtKey, txtLevel, txtAccess;
    private static final String LOG_TAG = "LogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_admin);
        checkConnectionInternet();
        session = new Session(FormAdminActivity.this);

        edNama = findViewById(R.id.ed_nama);
        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        edKey = findViewById(R.id.ed_key);

        rbGroupLevel = findViewById(R.id.rb_group_level);
        rbGroupAccess = findViewById(R.id.rb_group_akses);

        tvKey = findViewById(R.id.tv_key);
        btn = findViewById(R.id.btn);

        lyBack = findViewById(R.id.ly_back);

        rbGroupAccess.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbButtonAccess = findViewById(i);
                txtAccess = rbButtonAccess.getText().toString().toLowerCase();
                selectedRadioButtonIdOpsiAccess = rbGroupAccess.getCheckedRadioButtonId();
                switch (txtAccess)
                {
                    case "true":
                        tvKey.setVisibility(View.VISIBLE);
                        edKey.setVisibility(View.VISIBLE);
                        break;
                    case "false":
                        tvKey.setVisibility(View.GONE);
                        edKey.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rbGroupLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbButtonLevel = findViewById(i);
                txtLevel = rbButtonLevel.getText().toString().toLowerCase();
                selectedRadioButtonIdOpsiLevel = rbGroupLevel.getCheckedRadioButtonId();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNama = edNama.getText().toString();
                txtUsername = edUsername.getText().toString();
                txtPassword = edPassword.getText().toString();


                if(txtNama.trim().equals(""))
                {
                    edNama.setError("Harus Diisi");
                }else if(txtUsername.trim().equals(""))
                {
                    edUsername.setError("Harus Diisi");
                }else if(txtPassword.trim().equals(""))
                {
                    edPassword.setError("Harus Diisi");
                }else if(selectedRadioButtonIdOpsiLevel < 0)
                {
                    Toast.makeText(FormAdminActivity.this, "Opsi Level Harus Diisi", Toast.LENGTH_SHORT).show();
                }else if(selectedRadioButtonIdOpsiAccess == 0)
                {
                    Toast.makeText(FormAdminActivity.this, "Opsi Access Harus Diisi", Toast.LENGTH_SHORT).show();
                }else{
                    if(txtAccess.equals("true"))
                    {
                        txtKey = edKey.getText().toString();
                    }else{
                        txtKey = "0";
                    }

                    if(txtKey.trim().equals(""))
                    {
                        edKey.setError("Harus Diisi");
                    }else{
                        insertData();
                    }
                }
            }
        });

        lyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormAdminActivity.this, MainActivity.class);
                intent.putExtra("con_frag", 3);
                startActivity(intent);
                finish();
            }
        });

    }

    private void insertData()
    {
        APIRequestData ard = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseAdmins> insert = ard.insertDataAdmin(session.getToken(), session.getKey(), txtKey, txtNama, txtUsername, txtPassword, txtLevel, txtAccess);

        insert.enqueue(new Callback<ResponseAdmins>() {
            @Override
            public void onResponse(Call<ResponseAdmins> call, Response<ResponseAdmins> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(FormAdminActivity.this, "Data Berhasil Dimasukan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormAdminActivity.this, MainActivity.class);
                    intent.putExtra("con_frag", 3);
                    startActivity(intent);
                    finish();
                }else{
                    try {
                        Log.e(LOG_TAG, "error Form Donatur: "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAdmins> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(FormAdminActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Something Wrong : "+t.getMessage());
                pesan.show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(FormAdminActivity.this, MainActivity.class);
        intent.putExtra("con_frag", 3);
        startActivity(intent);
        finish();
    }

    public void checkConnectionInternet()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            androidx.appcompat.app.AlertDialog.Builder pesan = new AlertDialog.Builder(getApplicationContext());
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