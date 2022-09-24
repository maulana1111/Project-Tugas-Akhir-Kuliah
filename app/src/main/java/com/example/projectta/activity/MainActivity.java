package com.example.projectta.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectta.R;
import com.example.projectta.fragment.AccountFragment;
import com.example.projectta.fragment.AdminFragment;
import com.example.projectta.fragment.DonaturFragment;
import com.example.projectta.fragment.FileFragment;
import com.example.projectta.session.Session;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
//    Toolbar toolbar;
    TextView tv_title, namaAdmin;
    ImageView iv_menu;


    int kondisi_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kondisi_fragment = getIntent().getIntExtra("con_frag", 1);

        Session session = new Session(MainActivity.this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tv_title = findViewById(R.id.tv_title);
        iv_menu = findViewById(R.id.iv_menu);

        Menu menu = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);

        navigationView.bringToFront();
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        navigationView.setCheckedItem(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        namaAdmin = headerView.findViewById(R.id.nama_admin);

        if(session.getLoggedIn() != true)
        {
            namaAdmin.setText("Welcome");
        }else{
            namaAdmin.setText(session.getNamaAdmin());
        }

        switch (kondisi_fragment){
            case 1:
                DonaturLaunch();
                break;
            case 2:
                FileLaunch();
                break;
            case 3:
                AdminLaunch();
                break;
            case 4:
                AccountLaunch();
                break;
        }

    }


    private boolean onNavigationItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case  R.id.nav_admin:
                AdminLaunch();
                break;
            case  R.id.nav_home:
                DonaturLaunch();
                break;
            case  R.id.nav_file:
                FileLaunch();
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_account:
                AccountLaunch();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "Anda Berhasil Logout", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public void DonaturLaunch()
    {
        tv_title.setText("Donatur");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_for_fragment, new DonaturFragment())
                .commit();
    }

    public void AccountLaunch()
    {
        tv_title.setText("Account");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_for_fragment, new AccountFragment())
                .commit();
    }

    public void FileLaunch()
    {
        tv_title.setText("File");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_for_fragment, new FileFragment())
                .commit();
    }

    public void AdminLaunch()
    {
        tv_title.setText("Admin");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_for_fragment, new AdminFragment())
                .commit();
    }
}