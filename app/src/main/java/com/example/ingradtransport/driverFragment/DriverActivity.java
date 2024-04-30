package com.example.ingradtransport.driverFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.preferences.SharedPreferences;
import com.example.ingradtransport.model.User;
import com.google.android.material.navigation.NavigationView;

public class DriverActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        Toolbar toolbar = findViewById(R.id.toolbar_driver);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_driver);
        NavigationView navigationView = findViewById(R.id.nav_view_driver);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        View navHeader = navigationView.getHeaderView(0); //Отображение фамилии и имени авторизованного пользователя в хедере меню
        SharedPreferences pref= new SharedPreferences(this);
        User user = pref.getUser();
        TextView textLastnameMenu = (TextView) navHeader.findViewById(R.id.lastname_menu);
        TextView textNameMenu = (TextView) navHeader.findViewById(R.id.name_menu);
        textLastnameMenu.setText(user.getLastname());
        textNameMenu.setText(user.getName());


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, new HomeDriverFragment()).commit();
            navigationView.setCheckedItem(R.id.home_btn_menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        SharedPreferences pref= new SharedPreferences(this);
//        User user = pref.getUser();
//        TextView textLastnameMenu = findViewById(R.id.lastname_menu);
//        TextView textNameMenu = findViewById(R.id.name_menu);
//        //User user = new User();
//        textLastnameMenu.setText(user.getLastname());
//        textNameMenu.setText(user.getName());
        switch (item.getItemId()) {
            case R.id.home_btn_menu:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, new HomeDriverFragment()).commit();
                break;
            }
            case R.id.appl_btn_menu:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, new ApplicationDriverFragment()).commit();
                break;
            }
            case R.id.account_btn_menu:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, new AccountDriverFragment()).commit();
                break;
            }
            case R.id.settings_btn_menu:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, new SettingsDriverFragment()).commit();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}