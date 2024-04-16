package com.example.ingradtransport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.ingradtransport.bossfragment.BossActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    //private LoginViewModel viewModel;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //ActionBarDrawerToggle drawerToggle;
    ImageButton menu_button;

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return  true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu_button = findViewById(R.id.menu_button);

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem();
            }
        });





    }

    private void menuItem() {
        setContentView(R.layout.menu);
        drawerLayout = findViewById(R.id.drawer_layou);
        navigationView = findViewById(R.id.nav_vie);
        //drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav_drawer, R.string.close_nav_drawer);
        //drawerLayout.addDrawerListener(drawerToggle);
        //drawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_btn_menu:
                    {
                        break;
                    }
                    case R.id.appl_btn_menu:
                    {
                        Intent intent = new Intent(MainActivity.this, BossActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}