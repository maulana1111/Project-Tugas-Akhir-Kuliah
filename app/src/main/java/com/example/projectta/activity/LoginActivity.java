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
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseAdmin;
import com.example.projectta.session.Session;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView btnLogin;
    EditText edUsername, edPassword, edKey;
    String txtUsername, txtPassword, txtKey;


    private static final String LOG_TAG = "LogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        edKey = findViewById(R.id.ed_key);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUsername = edUsername.getText().toString();
                txtPassword = edPassword.getText().toString();
                txtKey = edKey.getText().toString();
                if(txtUsername.trim().equals(""))
                {
                    edUsername.setError("Harus Diisi");
                }else if(txtPassword.trim().equals(""))
                {
                    edPassword.setError("Harus Diisi");
                }else if(txtKey.trim().equals(""))
                {
                    edKey.setError("Harus Diisi");
                }else{
                    doLogin();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    private void doLogin()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseAdmin> processData = ardData.postLogin(txtUsername, txtPassword, txtKey);
        processData.enqueue(new Callback<ResponseAdmin>() {
            @Override
            public void onResponse(Call<ResponseAdmin> call, Response<ResponseAdmin> response) {

                    int statusCode = response.body().getStatus_code();
                    String statusMessage = response.body().getStatus_message();
                    if(statusCode != 200)
                    {
                        AlertDialog.Builder pesan = new AlertDialog.Builder(LoginActivity.this);
                        pesan.setTitle("Warning!");
                        pesan.setMessage("Error Code : "+Integer.toString(statusCode)+", Error Message : "+statusMessage);
                        pesan.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        pesan.setCancelable(false);
                        pesan.show();
                    }else{
                        Session session = new Session(LoginActivity.this);
                        session.setId(response.body().getId());
                        session.setNamaAdmin(response.body().getNama());
                        session.setLevelAdmin(response.body().getLevel());
                        session.setToken(response.body().getToken());
                        session.setKey(txtKey);
                        session.setLoggedIn(true);
                        Toast.makeText(LoginActivity.this, "Anda Berhasil Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("con_frag", 1);
                        startActivity(intent);
                        finish();
                    }

            }

            @Override
            public void onFailure(Call<ResponseAdmin> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal Menghubungi Server = "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkConnectionInternet() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            androidx.appcompat.app.AlertDialog.Builder pesan = new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this);
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