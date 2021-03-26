package com.openclassrooms.realestatemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.fragment.FragmentList;
import com.openclassrooms.realestatemanager.fragment.FragmentLoan;
import com.openclassrooms.realestatemanager.fragment.FragmentMap;
import com.openclassrooms.realestatemanager.fragment.FragmentSearch;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Handle fragments in bottom navigation view
        bottomNavBarFragmentsManagement();

        //Show list fragment by default
        displayFragment(new FragmentList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_btn) {
            // Start NewEstateActivity
            startActivity(new Intent(MainActivity.this, NewEstateActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    //Method to show specified fragment
    public void displayFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void bottomNavBarFragmentsManagement() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_list:
                        MainActivity.this.displayFragment(new FragmentList());
                        return true;
                    case R.id.navigation_map:
                        MainActivity.this.displayFragment(new FragmentMap());
                        return true;
                    case R.id.navigation_search:
                        MainActivity.this.displayFragment(new FragmentSearch());
                        return true;
                    case R.id.navigation_loan:
                        MainActivity.this.displayFragment(new FragmentLoan());
                        return true;
                }
                return false;
            }
        });
    }
}
