package com.example.projectta.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseDonaturFilter;
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

public class ShowPDFDonatur extends AppCompatActivity {

    WebView wb;
    TextView btnCancel, btnDownload;
    String from, to, opsi, key;
    Session session;
    List<DonaturModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdfdonatur);
        checkConnectionInternet();
        session = new Session(ShowPDFDonatur.this);

        wb = findViewById(R.id.webView);
        btnCancel = findViewById(R.id.btn_cancel);
        btnDownload = findViewById(R.id.btn_download);

        key = getIntent().getStringExtra("key");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        opsi = getIntent().getStringExtra("opsi");

        showPdf();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPDFDonatur.this, MainActivity.class);
                intent.putExtra("con_frag", 1);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showPdf()
    {
        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        rupiah.setDecimalFormatSymbols(formatRp);

        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseDonaturFilter> showData = ardData.GetDataFilter(session.getToken(), session.getKey(), key, from, to, opsi);
        showData.enqueue(new Callback<ResponseDonaturFilter>() {
            @Override
            public void onResponse(Call<ResponseDonaturFilter> call, Response<ResponseDonaturFilter> response) {
                if(response.isSuccessful())
                {
                    AlertDialog.Builder pesan = new AlertDialog.Builder(ShowPDFDonatur.this);
                    pesan.setTitle("Warning !!");

                    int param = response.body().getStatus_code();
                    String msgParam = response.body().getStatus_message();
//                    Toast.makeText(ShowPDFDonatur.this, "param : "+param+", msg param : "+msgParam, Toast.LENGTH_SHORT).show();
                    if(param == 200)
                    {
                        data = response.body().getData();
                        String msg = response.body().getStatus_key();

                        StringBuffer strData = new StringBuffer();
                        if(msg.equals("wrong"))
                        {
                            pesan.setMessage("Key Salah");
                            pesan.show();
                            for(int i=0; i < data.size(); i++)
                            {
                                strData.append(
                                        "<tr><td>"+data.get(i).getPemilik_rekening()+"</td><td>"+data.get(i).getJumlah()+"</td><td>"+data.get(i).getTanggal_donate()+"</td></tr>"
                                );
                            }
                        }else{
                            for(int i=0; i < data.size(); i++)
                            {
                                strData.append(
                                        "<tr><td>"+data.get(i).getPemilik_rekening()+"</td><td>"+rupiah.format(Integer.parseInt(data.get(i).getJumlah()))+"</td><td>"+data.get(i).getTanggal_donate()+"</td></tr>"
                                );
                            }
                        }



                        StringBuffer strAll = new StringBuffer();
                        strAll.append(
                                "<!DOCTYPE html>\n" +
                                        "<html>\n" +
                                        "<head>\n" +
                                        "\t<meta charset=\"utf-8\">\n" +
                                        "\t<title></title>\n" +
                                        "\t<style type=\"text/css\">\n" +
                                        "\t\t.h3-1{\n" +
                                        "\t\t\ttext-align: center;\n" +
                                        "\t\t}\n" +
                                        "\t\t.data{\n" +
                                        "\t\t\twidth: 100%;\n" +
                                        "\t\t\toverflow: hidden;\n" +
                                        "\t\t}\n" +
                                        "\t\t.data .cen{\n" +
                                        "\t\t\tfont-weight: bold;\n" +
                                        "\t\t}\n" +
                                        "\t\tth,td{\n" +
                                        "\t\t\tpadding: 10px;\n" +
                                        "\t\t}\n" +
                                        "\t\ttd{\n" +
                                        "\t\t\ttext-align: center;\n" +
                                        "font-size : 13px"+
                                        "\t\t}"+
                                        "table, th, td {\n" +
                                        "  border: 1px solid black;\n" +
                                        "  border-collapse: collapse;\n" +
                                        "}"+
                                        "\t</style>\n" +
                                        "</head>\n" +
                                        "<body>\n" +
                                        "\t<h3 class=\"h3-1\">Laporan Data Donatur</h3>\n" +
                                        "\t<div class=\"data\">\n" +
                                        "\t\t<div class=\"left\">\n" +
                                        "\t\t\t<br>\n" +
                                        "\t\t\t<span>Opsi Data : </span>\n" +
                                        "\t\t\t<span class=\"cen\">"+opsi+"</span>\n" +
                                        "\t\t\t<br>\n" +
                                        "\t\t\t<span>Dari Tanggal : </span>\n" +
                                        "\t\t\t<span class=\"cen\">"+from+"</span>\n" +
                                        "\t\t\t<br>\n" +
                                        "\t\t\t<span>Sampai Tanggal : </span>\n" +
                                        "\t\t\t<span class=\"cen\">"+to+"</span>\n" +
                                        "\t\t\t<br>\n" +
                                        "\t\t</div>\n" +
                                        "\t</div>\n" +
                                        "\t<hr style=\"border: 5px solid green;\">\n" +
                                        "\t<table border=\"1\">\n"+
                                        "<tr>\n" +
                                        "\t\t\t<th>Nama</th>\n" +
                                        "\t\t\t<th>Jumlah</th>\n" +
                                        "\t\t\t<th>Tanggal</th>\n" +
                                        "</tr>"
                        );

                        strAll.append(strData);

                        strAll.append(
                                "\t</table>\n" +
                                        "</body>\n" +
                                        "</html>\n"
                        );

                        String html = strAll.toString();

                        wb.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                    }else{
                        AlertDialog.Builder message = new AlertDialog.Builder(ShowPDFDonatur.this);
                        message.setTitle("Warning !!");
                        message.setMessage("Error Code : "+param+", Error Message : "+msgParam);
                        message.show();
                    }
                }else{
                    AlertDialog.Builder pesan = new AlertDialog.Builder(ShowPDFDonatur.this);
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
            public void onFailure(Call<ResponseDonaturFilter> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(ShowPDFDonatur.this);
                pesan.setTitle("Warning !!");
                pesan.setTitle("Error : "+t.getMessage());
                pesan.show();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf()
    {
        Context context = ShowPDFDonatur.this;
        PrintManager printManager = (PrintManager)ShowPDFDonatur.this.getSystemService(context.PRINT_SERVICE);
        PrintDocumentAdapter adapter = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            adapter = wb.createPrintDocumentAdapter();
        }
        String JobName = "laporan data donatur dari tanggal "+from+" sampai tanggal "+to;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            PrintJob printJob = printManager.print(JobName, adapter, new PrintAttributes.Builder().build());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShowPDFDonatur.this, MainActivity.class);
        intent.putExtra("con_frag", 1);
        startActivity(intent);
        finish();
    }

    public void checkConnectionInternet() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            androidx.appcompat.app.AlertDialog.Builder pesan = new AlertDialog.Builder(ShowPDFDonatur.this);
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