package com.example.rwado.financialcalc;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fm = getSupportFragmentManager();
        Loans mLoansFragment = new Loans();
        fm.beginTransaction().replace(R.id.switch_fragment, mLoansFragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();

        if (id == R.id.nav_loans) {
            Loans mLoansFragment = new Loans();
            fm.beginTransaction().replace(R.id.switch_fragment, mLoansFragment).commit();
        } else if (id == R.id.nav_investments) {
            Investments mInvestmentsFragment = new Investments();
            fm.beginTransaction().replace(R.id.switch_fragment, mInvestmentsFragment).commit();
        } else if (id == R.id.nav_leasings) {
            Leasings mLeasingFragment = new Leasings();
            fm.beginTransaction().replace(R.id.switch_fragment, mLeasingFragment).commit();
        } else if (id == R.id.nav_transfers) {
            Transfers mTransfersFragment = new Transfers();
            fm.beginTransaction().replace(R.id.switch_fragment, mTransfersFragment).commit();
        } else if (id == R.id.nav_atms) {
            Atms mAtmsFragment = new Atms();
            fm.beginTransaction().replace(R.id.switch_fragment, mAtmsFragment).commit();
        } else if (id == R.id.nav_info) {
            Toast.makeText(this, R.string.app_made_by, Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
