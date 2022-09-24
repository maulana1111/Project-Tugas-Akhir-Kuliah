package com.example.projectta.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseDonatur;
import com.example.projectta.activity.Detail_Donatur_activity;
import com.example.projectta.activity.FormDonaturActivity;
import com.example.projectta.activity.LoginActivity;
import com.example.projectta.activity.MainActivity;
import com.example.projectta.activity.ShowPDFDonatur;
import com.example.projectta.adapter.AdapterDonatur;
import com.example.projectta.model.DonaturModel;
import com.example.projectta.session.Session;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonaturFragment extends Fragment {

    FloatingActionButton fab;
    LinearLayout addFab, docFab, pageError, lyData;
    Boolean isAllFabsVisible;
    RecyclerView recyclerTerima, recyclerProses, recyclerTolak;

    SwipeRefreshLayout swipeRefreshLayout;
    ScrollView scrollView;
    ProgressBar pbData;
    Session session;

    EditText edFrom, edTo, edKey;

    RadioGroup rbGroup;
    RadioButton radioButton;

    String txtFrom, txtTo, txtSelectedRbOpsi, txtKey;

    List<DonaturModel> listDataTerima = new ArrayList<>();
    List<DonaturModel> listDataProses = new ArrayList<>();
    List<DonaturModel> listDataTolak = new ArrayList<>();
    AlertDialog.Builder alert;
    private static final String LOG_TAG = "LogActivity";

    public DonaturFragment() {
        // Required empty public constructor
    }

    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        showData();
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                Session seasson = new Session(getContext());

                    AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                    pesan.setTitle("Warning!!");
                    pesan.setMessage("Apakah Anda Yakin Ingin Keluar ?");
                    pesan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            seasson.logout();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    });
                    pesan.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    pesan.show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donatur, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        checkConnectionInternet();


        session = new Session(getContext());
        fab = view.findViewById(R.id.fab);
        addFab = view.findViewById(R.id.fab_add);
        docFab = view.findViewById(R.id.fab_doc);
        pageError = view.findViewById(R.id.error_page);
        lyData = view.findViewById(R.id.ly_data);

        recyclerTerima = view.findViewById(R.id.recycler_terima);
        recyclerProses = view.findViewById(R.id.recycler_proses);
        recyclerTolak = view.findViewById(R.id.recycler_tolak);

        swipeRefreshLayout = view.findViewById(R.id.srl_data);
        pbData = view.findViewById(R.id.pb_data);
        scrollView = view.findViewById(R.id.scroll_view);
        alert = new AlertDialog.Builder(getContext());

        isAllFabsVisible = false;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAllFabsVisible)
                {
                    addFab.setVisibility(View.VISIBLE);
                    docFab.setVisibility(View.VISIBLE);

                    isAllFabsVisible = true;
                }else{
                    addFab.setVisibility(View.GONE);
                    docFab.setVisibility(View.GONE);

                    isAllFabsVisible = false;
                }
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FormDonaturActivity.class));
                getActivity().finish();
            }
        });

        docFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View promp = inflater.inflate(R.layout.dialog_filter, null);
                alert.setView(promp);

                edKey = promp.findViewById(R.id.ed_key);
                rbGroup = promp.findViewById(R.id.rb_group);
                edFrom = promp.findViewById(R.id.ed_from);
                edTo = promp.findViewById(R.id.ed_to);

                edFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        int nowYear = calendar.get(Calendar.YEAR);
                        int nowMonth = calendar.get(Calendar.MONTH);
                        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, month, dayOfMonth);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                edFrom.setText(dateFormat.format(cal.getTime()));
                            }
                        }, nowYear, nowMonth, nowDay);
                        dialog.show();
                    }
                });

                edTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        int nowYear = calendar.get(Calendar.YEAR);
                        int nowMonth = calendar.get(Calendar.MONTH);
                        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, month, dayOfMonth);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                edTo.setText(dateFormat.format(cal.getTime()));
                            }
                        }, nowYear, nowMonth, nowDay);
                        dialog.show();
                    }
                });

                alert.setCancelable(false);
                alert.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedRadioButtonOpsi = rbGroup.getCheckedRadioButtonId();

                        txtFrom = edFrom.getText().toString();
                        txtTo = edTo.getText().toString();
                        txtKey = edKey.getText().toString();
                        if(txtFrom.trim().equals(""))
                        {
                            edFrom.setError("Tidak Boleh Kosong");
                        }else if(txtTo.trim().equals(""))
                        {
                            edTo.setError("Tidak Boleh Kosong");
                        }else if(selectedRadioButtonOpsi < 0){
                            Toast.makeText(getActivity(), "Opsi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                        }else if(txtKey.trim().equals(""))
                        {
                            edKey.setError("Tidak Boleh Kosong");
                        }else{
                            radioButton = promp.findViewById(selectedRadioButtonOpsi);
                            txtSelectedRbOpsi = radioButton.getText().toString().toLowerCase();
                            Intent intent = new Intent(getContext(), ShowPDFDonatur.class);
                            intent.putExtra("from", txtFrom);
                            intent.putExtra("to", txtTo);
                            intent.putExtra("opsi", txtSelectedRbOpsi);
                            intent.putExtra("key", txtKey);
                            startActivity(intent);
                            getActivity().finish();
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    public void refreshData()
    {
        int sec = 1000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showData();
//                Toast.makeText(getActivity(), "refresing...", Toast.LENGTH_SHORT).show();
            }
        };
        handler.postDelayed(runnable, sec);
    }

    private void showData()
    {
        pbData.setVisibility(View.VISIBLE);
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseDonatur> showData = ardData.getDataDonatur(session.getToken(), session.getKey());
        showData.enqueue(new Callback<ResponseDonatur>() {
            @Override
            public void onResponse(Call<ResponseDonatur> call, Response<ResponseDonatur> response) {

                if(response.isSuccessful())
                {
                    int status_code = response.body().getStatus_code();

                    listDataProses = response.body().getData_proses();
                    listDataTerima = response.body().getData_diterima();
                    listDataTolak = response.body().getData_ditolak();

                    if(status_code != 200)
                    {
                        lyData.setVisibility(View.GONE);
                        pageError.setVisibility(View.VISIBLE);
                        AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("You don't Have Permission To Access, Your Key Is Wrong");
                        pesan.show();
                        fab.setVisibility(View.GONE);
                        pbData.setVisibility(View.GONE);
                    }else{
                        fab.setVisibility(View.VISIBLE);
                        if(!listDataTerima.isEmpty())
                        {
                            AdapterDonatur adapterDonaturTerima = new AdapterDonatur(getContext(), listDataTerima);
                            recyclerTerima.setVisibility(View.VISIBLE);
                            recyclerTerima.setAdapter(adapterDonaturTerima);
                            LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerTerima.setLayoutManager(verticalLayoutManagaer);
                            recyclerTerima.setHasFixedSize(true);
                            recyclerTerima.addItemDecoration(new DividerItemDecoration(getContext(), 1));
                        }else{
                            recyclerTerima.setVisibility(View.GONE);
                        }

                        if(!listDataProses.isEmpty())
                        {
                            recyclerProses.setVisibility(View.VISIBLE);
                            recyclerProses.setAdapter(new AdapterDonatur(getContext(), listDataProses));
                            LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerProses.setLayoutManager(verticalLayoutManagaer);
                            recyclerProses.setHasFixedSize(true);
                            recyclerProses.addItemDecoration(new DividerItemDecoration(getContext(), 1));
                        }else{
                            recyclerProses.setVisibility(View.GONE);
                        }

                        if(!listDataTolak.isEmpty())
                        {
                            recyclerTolak.setVisibility(View.VISIBLE);
                            recyclerTolak.setAdapter(new AdapterDonatur(getContext(), listDataTolak));
                            LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerTolak.setLayoutManager(verticalLayoutManagaer);
                            recyclerTolak.setHasFixedSize(true);
                            recyclerTolak.addItemDecoration(new DividerItemDecoration(getContext(), 1));
                        }else{
                            recyclerTolak.setVisibility(View.GONE);
                        }

                        pbData.setVisibility(View.GONE);

                    }
                }else{
                    try {
                        Log.e(LOG_TAG, "Error :"+response.errorBody().string());
                        pbData.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseDonatur> call, Throwable t) {
                Toast.makeText(getContext(), "Tidak Dapat Menghubungi Server = "+t.getMessage(), Toast.LENGTH_SHORT).show();
                pbData.setVisibility(View.GONE);
            }
        });
//        refreshData();
    }

    public void checkConnectionInternet() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            androidx.appcompat.app.AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
            pesan.setTitle("Informasi");
            pesan.setMessage("Koneksi Internet Terputus");
            pesan.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                    System.exit(0);
                }
            });
            pesan.show();
        }
    }
}