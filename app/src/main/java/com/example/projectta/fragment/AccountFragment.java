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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.API.APIRequestData;
import com.example.projectta.API.RetroServer;
import com.example.projectta.R;
import com.example.projectta.Response.ResponseAdmins;
import com.example.projectta.Response.ResponseUbahAdmin;
import com.example.projectta.activity.DetailAdminActivity;
import com.example.projectta.activity.FormAdminActivity;
import com.example.projectta.activity.MainActivity;
import com.example.projectta.model.ModelAdmin;
import com.example.projectta.session.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {


    LinearLayout lyData, lyError;
    EditText edNama, edPassword, edUsername, edKey;
    TextView btn;
    String txtNama, txtPassword, txtUsername, txtKey;
    Session session;
    List<ModelAdmin> data = new ArrayList<>();

    public AccountFragment() {

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
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        checkConnectionInternet();
        session  = new Session(getContext());

        edNama = view.findViewById(R.id.ed_nama);
        edPassword = view.findViewById(R.id.ed_password);
        edUsername = view.findViewById(R.id.ed_username);
        edKey = view.findViewById(R.id.ed_key);
        btn = view.findViewById(R.id.btn);

        lyData = view.findViewById(R.id.ly_data);
        lyError = view.findViewById(R.id.ly_error);


        showData();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNama = edNama.getText().toString();
                txtUsername = edUsername.getText().toString();
                txtPassword = edPassword.getText().toString();
                txtKey = edKey.getText().toString();

                if(txtNama.trim().equals(""))
                {
                    edNama.setError("Tidak Boleh Kosong");
                }else if(txtUsername.trim().equals(""))
                {
                    edUsername.setError("Tidak Boleh Kosong");
                }else{
                    if(txtPassword.trim().equals(""))
                    {
                        txtPassword = "0";
                    }
                    if(txtKey.trim().equals(""))
                    {
                        txtKey = session.getKey();
                    }

                    if((!txtPassword.trim().equals("")) && (!txtKey.trim().equals("")))
                    {
//                        Toast.makeText(getContext(), "key = "+txtKey, Toast.LENGTH_SHORT).show();
                        updateData();
                    }
                }
            }
        });

    }

    private void updateData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseUbahAdmin> showData = ardData.updateData(session.getToken(), session.getKey(), session.getId(), txtNama, txtUsername, txtPassword, txtKey);
        showData.enqueue(new Callback<ResponseUbahAdmin>() {
            @Override
            public void onResponse(Call<ResponseUbahAdmin> call, Response<ResponseUbahAdmin> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getContext(), "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                    int id = session.getId();
                    String namaAdmin = txtNama;
                    String levelAdmin = session.getLevelAdmin();
                    String token = response.body().getStatus_token();
                    String key = txtKey;
                    Boolean status = true;

                    session.logout();

                    session.setId(id);
                    session.setNamaAdmin(namaAdmin);
                    session.setLevelAdmin(levelAdmin);
                    session.setKey(key);
                    session.setToken(token);
                    session.setLoggedIn(true);

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("con_frag", 1);
                    startActivity(intent);
                    getActivity().finish();
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
            public void onFailure(Call<ResponseUbahAdmin> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                pesan.setTitle("Warning !!");
                pesan.setMessage("Error Server : "+t.getCause());
                pesan.show();
            }
        });
    }

    private void showData()
    {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseAdmins> showData = ardData.getDetailAdmin(session.getToken(), session.getKey(), session.getId());
        showData.enqueue(new Callback<ResponseAdmins>() {
            @Override
            public void onResponse(Call<ResponseAdmins> call, Response<ResponseAdmins> response) {
                if(response.isSuccessful())
                {
                    int param = response.body().getStatus_code();
                    if(param != 200)
                    {
                        lyData.setVisibility(View.GONE);
                        lyError.setVisibility(View.VISIBLE);
                        AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                        pesan.setTitle("Warning !!");
                        pesan.setMessage("You don't Have Permission To Access, Your Key Is Wrong");
                        pesan.show();
                    }else{
                        lyData.setVisibility(View.VISIBLE);
                        lyError.setVisibility(View.GONE);

                        data = response.body().getData();
                        edNama.setText(data.get(0).getNama());
                        edUsername.setText(data.get(0).getUsername());
                        edPassword.setHint("Kosongkan Jika Tidak Ingin Diubah");
                        edKey.setHint("Kosongkan Jika Tidak Ingin Diubah");
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
            public void onFailure(Call<ResponseAdmins> call, Throwable t) {
                AlertDialog.Builder pesan = new AlertDialog.Builder(getContext());
                pesan.setTitle("Warning !!");
                pesan.setMessage("Error Server : "+t.getMessage());
                pesan.show();
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