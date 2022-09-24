package com.example.projectta.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseFIle;
import com.example.projectta.activity.FormDonaturActivity;
import com.example.projectta.activity.FormFileActivity;
import com.example.projectta.activity.MainActivity;
import com.example.projectta.adapter.AdapterDonatur;
import com.example.projectta.adapter.AdapterFile;
import com.example.projectta.model.ModelFile;
import com.example.projectta.session.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileFragment extends Fragment {

    RecyclerView recycler;

    ProgressBar pbData;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout fab, lyData, lyError;
    Session session;

    List<ModelFile> data = new ArrayList<>();

    public FileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("con_frag", 1);
                startActivity(intent);
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnectionInternet();
        session = new Session(getContext());
        recycler = view.findViewById(R.id.recycler);
        pbData = view.findViewById(R.id.pb_data);
        swipeRefreshLayout = view.findViewById(R.id.srl_data);
        fab = view.findViewById(R.id.fab_add);
        lyData = view.findViewById(R.id.ly_data);
        lyError = view.findViewById(R.id.ly_error);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FormFileActivity.class));
                getActivity().finish();
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


    private void showData()
    {
        pbData.setVisibility(View.VISIBLE);
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseFIle> show = ardData.getDataFiles(session.getToken(), session.getKey());
        show.enqueue(new Callback<ResponseFIle>() {
            @Override
            public void onResponse(Call<ResponseFIle> call, Response<ResponseFIle> response) {
                if(response.isSuccessful())
                {
                    int status_code = response.body().getStatus_code();
                    if(status_code != 200)
                    {
                        lyData.setVisibility(View.GONE);
                        lyError.setVisibility(View.VISIBLE);
                        AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("You don't Have Permission To Access, Your Key Is Wrong");
                        pesan.show();
                        fab.setVisibility(View.GONE);
                        pbData.setVisibility(View.GONE);
                    }else{
                        lyData.setVisibility(View.VISIBLE);
                        lyError.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);

                        data = response.body().getData();
                        if(!data.isEmpty())
                        {
                            recycler.setVisibility(View.VISIBLE);
                            recycler.setVisibility(View.VISIBLE);
                            recycler.setAdapter(new AdapterFile(getContext(), data));
                            LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recycler.setLayoutManager(verticalLayoutManagaer);
                            recycler.setHasFixedSize(true);
                            recycler.addItemDecoration(new DividerItemDecoration(getContext(), 1));
                            pbData.setVisibility(View.GONE);
                        }else{
                            AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                            pesan.setTitle("Warning !!");
                            pesan.setMessage("Data Kosong");
                            pesan.show();
                            recycler.setVisibility(View.GONE);
                            pbData.setVisibility(View.GONE);
                        }

                    }
                }else{
                    AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
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
            public void onFailure(Call<ResponseFIle> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                pesan.setTitle("Warning !!");
                pesan.setMessage("Error Server : "+t.getMessage());
                pesan.show();
                pbData.setVisibility(View.GONE);
            }
        });
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