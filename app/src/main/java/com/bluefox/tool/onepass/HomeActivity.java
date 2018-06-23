package com.bluefox.tool.onepass;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HomeActivity extends DrawerActivity implements HomeFragment.OnFragmentInteractionListener, SiteListFragment.OnFragmentInteractionListener {
    private HomeFragment homeFragment;
    private SiteListFragment siteListFragment;

    private int fragmentKind = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_home);

        this.setOnDrawerMenuClickListener(new OnDrawerMenuClickListener() {
            @Override
            public void onNavigateToHome() {
                loadHome();
            }

            @Override
            public void onNavigateToSiteList(int level) {
                loadSiteList(level);
            }
        });

        if (savedInstanceState != null) {
            fragmentKind = savedInstanceState.getInt("fragmentKind");
        }
        if (fragmentKind == 0) {
            loadHome();
        }
        else {
            loadSiteList(fragmentKind);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragmentKind", fragmentKind);
    }

    private void loadHome() {
        setTitle("Home");
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //init home fragment
        homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.homeLayout, homeFragment);
        fragmentTransaction.commit();

        fragmentKind = 0;
    }

    private void loadSiteList(int level) {
        setTitle("Level " + level);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //init site list fragment
        siteListFragment = new SiteListFragment();
        siteListFragment.setArguments(SiteListFragment.generateParams(0, level, null));
        fragmentTransaction.replace(R.id.homeLayout, siteListFragment);
        fragmentTransaction.commit();

        fragmentKind = level;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
