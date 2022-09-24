package com.example.projectta.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseDonaturDetail;
import com.example.projectta.session.Session;
import com.example.projectta.session.SessionTemp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormDonaturActivity extends AppCompatActivity {

    EditText edNoRekening, edPemilikRekening, edOrganisasi, edJumlah, edGmail, edPesan, edTanggal, edKey;
    TextView btnNext1, btnNext2, btnBack1, btnBack2, tvKey;
    ImageView ivImage;
    String txtNorek, txtPemilik, txtOrg, txtJum, txtGmail, txtPesan, txtTanggal, txtOpsi, txtKey;
    String part_image = "";
    Session session;
    SessionTemp sessionTemp;
    LinearLayout step1, step2, lyBack;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    final int REQUEST_GALLERY = 9544;
    int ly = 1;
    int selectedRadioButtonIdOpsi;
    private static final String LOG_TAG = "LogActivity";

    RadioGroup rbGroup;
    RadioButton rbOpsi;
    int lyCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_donatur);
        checkConnectionInternet();
        session = new Session(FormDonaturActivity.this);
        sessionTemp = new SessionTemp(FormDonaturActivity.this);

        edNoRekening = findViewById(R.id.ed_no_rekening);
        edPemilikRekening = findViewById(R.id.ed_pemilik_rekening);
        edOrganisasi = findViewById(R.id.ed_organisasi);
        edJumlah = findViewById(R.id.ed_jumlah);
        edGmail = findViewById(R.id.ed_gmail);
        edPesan = findViewById(R.id.ed_pesan);
        edTanggal = findViewById(R.id.ed_tanggal);
        edKey = findViewById(R.id.ed_key);
        tvKey = findViewById(R.id.tv_key);

        rbGroup = findViewById(R.id.rb_group);

        btnBack1 = findViewById(R.id.btn_back1);
        btnBack2 = findViewById(R.id.btn_back2);
        btnNext1 = findViewById(R.id.btn_next1);
        btnNext2 = findViewById(R.id.btn_next2);

        ivImage = findViewById(R.id.iv_bukti);

        step1 = findViewById(R.id.ly_step1);
        step2 = findViewById(R.id.ly_step2);
        lyBack = findViewById(R.id.ly_back);

        edTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                int nowYear = calendar.get(Calendar.YEAR);
                int nowMonth = calendar.get(Calendar.MONTH);
                int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(FormDonaturActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        edTanggal.setText(dateFormat.format(cal.getTime()));
                    }
                }, nowYear, nowMonth, nowDay);
                dialog.show();
            }
        });

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int selectedId) {
                rbOpsi = findViewById(selectedId);
                txtOpsi = rbOpsi.getText().toString();
                selectedRadioButtonIdOpsi = rbGroup.getCheckedRadioButtonId();
                switch (txtOpsi)
                {
                    case "True":
                        tvKey.setVisibility(View.VISIBLE);
                        edKey.setVisibility(View.VISIBLE);
                        break;
                    case "False":
                        tvKey.setVisibility(View.GONE);
                        edKey.setVisibility(View.GONE);
                        break;
                }
            }
        });

        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyCount++;
                txtNorek = edNoRekening.getText().toString();
                txtPemilik = edPemilikRekening.getText().toString();
                txtOrg = edOrganisasi.getText().toString();
                txtJum = edJumlah.getText().toString();
                txtGmail = edGmail.getText().toString();
                txtPesan = edPesan.getText().toString();
                txtTanggal = edTanggal.getText().toString();

                if(txtNorek.trim().equals(""))
                {
                    edNoRekening.setError("Tidak Boleh Kosong");
                }else if(txtPemilik.trim().equals(""))
                {
                    edPemilikRekening.setError("Tidak Boleh Kosong");
                }else if(txtOrg.trim().equals(""))
                {
                    edOrganisasi.setError("Tidak Boleh Kosong");
                }else if(txtJum.trim().equals(""))
                {
                    edJumlah.setError("Tidak Boleh Kosong");
                }else if(txtGmail.trim().equals(""))
                {
                    edGmail.setError("Tidak Boleh Kosong");
                }else if(txtPesan.trim().equals(""))
                {
                    edPesan.setError("Tidak Boleh Kosong");
                }else if(txtTanggal.trim().equals(""))
                {
                    edTanggal.setError("Tidak Boleh Kosong");
                }else if(selectedRadioButtonIdOpsi == 0){
                    Toast.makeText(FormDonaturActivity.this, "Opsi Tidak Boleh kosong : "+selectedRadioButtonIdOpsi, Toast.LENGTH_SHORT).show();
                }else{
                    sessionTemp.setNoRekening(txtNorek);
                    sessionTemp.setNamaPemilik(txtPemilik);
                    sessionTemp.setOrg(txtOrg);
                    sessionTemp.setJum(txtJum);
                    sessionTemp.setGmail(txtGmail);
                    sessionTemp.setPesan(txtPesan);
                    sessionTemp.setTanggal(txtTanggal);
                    sessionTemp.setOpsi(txtOpsi);
                    if(txtOpsi.equals("True"))
                    {
                        txtKey = edKey.getText().toString();
                    }else{
                        txtKey = "0";
                    }
                    sessionTemp.setkey_temp(txtKey);
                    step1.setVisibility(View.GONE);
                    step2.setVisibility(View.VISIBLE);
                }

            }
        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyCount--;
                Intent intent = new Intent(FormDonaturActivity.this, MainActivity.class);
                intent.putExtra("con_frag", 1);
                startActivity(intent);
                finish();
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyCount--;
                step1.setVisibility(View.VISIBLE);
                step2.setVisibility(View.GONE);
            }
        });

        lyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormDonaturActivity.this, MainActivity.class);
                intent.putExtra("con_frag", 1);
                startActivity(intent);
                finish();
            }
        });

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }else{
                        pickImageFromGallery();
                    }
                }else{
                    pickImageFromGallery();
                }
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(part_image.trim().equals(""))
                {
                    AlertDialog.Builder pesan = new AlertDialog.Builder(FormDonaturActivity.this);
                    pesan.setTitle("Warning !!");
                    pesan.setMessage("Gambar Tidak Boleh Kosong");
                    pesan.show();
                }else{
                    insert_data();
                    sessionTemp.hapus();
                }
            }
        });


    }

    private void insert_data()
    {
        File imageFile = new File(part_image);
        RequestBody reqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("bukti_transfer", imageFile.getName(), reqBody);
        RequestBody req_token = MultipartBody.create(MultipartBody.FORM, session.getToken());
        RequestBody req_key_token = MultipartBody.create(MultipartBody.FORM, session.getKey());
        RequestBody req_norek = MultipartBody.create(MultipartBody.FORM, sessionTemp.getNorek());
        RequestBody req_pemilik_norek = MultipartBody.create(MultipartBody.FORM, sessionTemp.getNamaPemilik());
        RequestBody req_org = MultipartBody.create(MultipartBody.FORM, sessionTemp.getOrg());
        RequestBody req_jum = MultipartBody.create(MultipartBody.FORM, sessionTemp.getjum());
        RequestBody req_gmail = MultipartBody.create(MultipartBody.FORM, sessionTemp.getGmail());
        RequestBody req_pesan = MultipartBody.create(MultipartBody.FORM, sessionTemp.getPesan());
        RequestBody req_tanggal_donate = MultipartBody.create(MultipartBody.FORM, sessionTemp.getTanggal());
        RequestBody req_opsi = MultipartBody.create(MultipartBody.FORM, sessionTemp.getOpsi());
        RequestBody req_key = MultipartBody.create(MultipartBody.FORM, sessionTemp.getkey_temp());

        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseDonaturDetail> insertData = ardData.InsertDataDonatur(partImage, req_token, req_key_token, req_norek, req_pemilik_norek, req_org, req_jum, req_gmail, req_pesan, req_tanggal_donate, req_opsi, req_key);
        insertData.enqueue(new Callback<ResponseDonaturDetail>() {
            @Override
            public void onResponse(Call<ResponseDonaturDetail> call, Response<ResponseDonaturDetail> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(FormDonaturActivity.this, "Data Berhasil Dimasukan, Dengan Hasil Eksekusi = "+response.body().getExecution_time(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormDonaturActivity.this, MainActivity.class);
                    intent.putExtra("con_frag", 1);
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
            public void onFailure(Call<ResponseDonaturDetail> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(FormDonaturActivity.this);
                pesan.setTitle("Warning !!");
                pesan.setMessage("Something Wrong : "+t.getMessage());
                pesan.show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        lyCount--;
        if(lyCount == 0)
        {
            Intent intent = new Intent(FormDonaturActivity.this, MainActivity.class);
            intent.putExtra("con_frag", 1);
            startActivity(intent);
            finish();
        }else if(lyCount == 1)
        {
            step1.setVisibility(View.VISIBLE);
            step2.setVisibility(View.GONE);
        }
    }

    private void pickImageFromGallery()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if(requestCode == REQUEST_GALLERY)
            {
                try{
                    Uri dataimage = data.getData();
                    String[] imageProjection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(dataimage, imageProjection, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        int indexImage = cursor.getColumnIndex(imageProjection[0]);
                        part_image = cursor.getString(indexImage);
                        if (part_image != null) {
                            File image = new File(part_image);
                            ivImage.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
                        }
                    }
                }catch (Exception e)
                {
                    Log.e(LOG_TAG, "error When uplaod");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickImageFromGallery();
                }else{
                    Toast.makeText(FormDonaturActivity.this, "Permission denied!!", Toast.LENGTH_SHORT).show();
                }
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