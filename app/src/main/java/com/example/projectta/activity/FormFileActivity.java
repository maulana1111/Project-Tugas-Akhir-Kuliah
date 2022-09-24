package com.example.projectta.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseFIle;
import com.example.projectta.session.Session;
import com.example.projectta.session.SessionTemp;
import com.example.projectta.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormFileActivity extends AppCompatActivity {

    EditText edKeterangan, edKey;
    LinearLayout lyEd, lyFile, lyBtn;
    TextView btnNext1, btnNext2, btnBack1, btnBack2, tv_judul;
    String txtKeterangan, txtKey, txt_part_file, txt_part_file2;
    int lyCount = 1;
    SessionTemp sessionTemp;
    Session session;
    ImageView chooseFile;
    private static final int PERMISSION_CODE = 1001;
    final int REQUEST_FILE = 9544;
    private static final String LOG_TAG = "LogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_file);
        checkConnectionInternet();
        sessionTemp = new SessionTemp(FormFileActivity.this);
        session = new Session(FormFileActivity.this);

        edKeterangan = findViewById(R.id.ed_keterangan);
        edKey = findViewById(R.id.ed_key);

        lyEd = findViewById(R.id.ly_ed);
        lyFile = findViewById(R.id.ly_file);
        chooseFile = findViewById(R.id.iv_choose_file);

        btnNext1 = findViewById(R.id.btn_next1);
        btnNext2 = findViewById(R.id.btn_next2);
        btnBack1 = findViewById(R.id.btn_back1);
        btnBack2 = findViewById(R.id.btn_back2);
        lyBtn = findViewById(R.id.ly_btn);
        tv_judul = findViewById(R.id.tv_judul);

        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyCount++;
                txtKeterangan = edKeterangan.getText().toString();
                txtKey = edKey.getText().toString();
                if(txtKeterangan.trim().equals(""))
                {
                    edKeterangan.setError("Harus Diisi");
                }else if(txtKey.trim().equals("")){
                    edKey.setError("Harus Diisi");
                }else{
                    sessionTemp.setKeterangan(txtKeterangan);
                    sessionTemp.setkey_temp(txtKey);

                    lyEd.setVisibility(View.GONE);
                    lyFile.setVisibility(View.VISIBLE);
                }
            }
        });

        lyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormFileActivity.this, MainActivity.class);
                intent.putExtra("con_frag", 2);
                startActivity(intent);
                finish();
            }
        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyCount--;
                Intent intent = new Intent(FormFileActivity.this, MainActivity.class);
                intent.putExtra("con_frag", 2);
                startActivity(intent);
                finish();
            }
        });

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }else{
                        pickFile();
                    }
                }else{
                    pickFile();
                }
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File externalStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                File file = new File(externalStorageDirectory+"/"+txt_part_file);
                RequestBody reqBody = RequestBody.create(MediaType.parse("*/*"), file);

                MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), reqBody);


                RequestBody req_token = MultipartBody.create(MultipartBody.FORM, session.getToken());
                RequestBody req_key_token = MultipartBody.create(MultipartBody.FORM, session.getKey());
                RequestBody reqKeterangan = MultipartBody.create(MultipartBody.FORM, sessionTemp.getKeterangan());
                RequestBody reqKey = MultipartBody.create(MultipartBody.FORM, sessionTemp.getkey_temp());

                Toast.makeText(FormFileActivity.this, "hasil "+file.getName(), Toast.LENGTH_SHORT).show();

                APIRequestData ard = RetroServer.connectRetrofit().create(APIRequestData.class);
                Call<ResponseFIle> insertData = ard.InsertDataFile(partFile, req_token, req_key_token, reqKeterangan, reqKey);
                insertData.enqueue(new Callback<ResponseFIle>() {
                    @Override
                    public void onResponse(Call<ResponseFIle> call, Response<ResponseFIle> response) {
                        if(response.isSuccessful())
                        {
                            int code = response.body().getStatus_code();
                            if(code != 200)
                            {
                                Toast.makeText(FormFileActivity.this, "Data Berhasil Dimasukan, dengan hasil eksekusi = "+response.body().getExecution_time()+" Sec", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FormFileActivity.this, MainActivity.class);
                                intent.putExtra("con_frag", 2);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(FormFileActivity.this, response.body().getStatus_message(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FormFileActivity.this, MainActivity.class);
                                intent.putExtra("con_frag", 2);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            try {
                                Log.e(LOG_TAG, "error Form file: "+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseFIle> call, Throwable t) {
                        AlertDialog.Builder pesan = new AlertDialog.Builder(FormFileActivity.this);
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("Something Wrong : "+t.getMessage());
                        pesan.show();
                    }
                });
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyCount--;
                lyEd.setVisibility(View.VISIBLE);
                lyFile.setVisibility(View.GONE);
            }
        });

    }



    private void pickFile()
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose File");
        startActivityForResult(chooseFile, 10);

    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            switch (requestCode)
            {
                case 10:
                    if(resultCode == RESULT_OK)
                    {
                        Uri uri = data.getData();
                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if(cursor != null) {
                            cursor.moveToFirst();
                            txt_part_file = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            if (txt_part_file != null) {
                                File file = new File(txt_part_file);
                                txt_part_file2 = file.getPath();
                                cursor.close();
                            }
                        }
                        tv_judul.setText(getFileName(uri, getApplicationContext()));
                    }
            }
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri, Context context)
    {
        String res = null;
        if(uri.getScheme().equals("content"))
        {
            Cursor cursor = context.getContentResolver().query(uri, null,null,null,null);
            try {
                if(cursor!=null && cursor.moveToFirst())
                {
                    res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                cursor.close();
            }
            if(res == null)
            {
                res = uri.getPath();
                int cutt = res.lastIndexOf('/');
                if(cutt != -1)
                {
                    res = res.substring(cutt + 1);
                }
            }
        }
        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickFile();
                }else{
                    Toast.makeText(FormFileActivity.this, "Permission denied!!", Toast.LENGTH_SHORT).show();
                }
        }
    }



    @Override
    public void onBackPressed()
    {
        lyCount--;
        if(lyCount == 0)
        {
            Intent intent = new Intent(FormFileActivity.this, MainActivity.class);
            intent.putExtra("con_frag", 2 );
            startActivity(intent);
            finish();
        }else if(lyCount == 1)
        {
            lyEd.setVisibility(View.VISIBLE);
            lyFile.setVisibility(View.GONE);
        }
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