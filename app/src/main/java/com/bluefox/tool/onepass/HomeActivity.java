package com.bluefox.tool.onepass;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class HomeActivity extends DrawerActivity implements HomeFragment.OnFragmentInteractionListener, SiteListFragment.OnFragmentInteractionListener {
    private HomeFragment homeFragment;
    private SiteListFragment siteListFragment;

    private int fragmentKind = -1;

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
            int savedFragmentKind = savedInstanceState.getInt("fragmentKind");
            if (savedFragmentKind == 0) {
                loadHome();
            }
            else {
                loadSiteList(savedFragmentKind);
            }
        }
        else {
            loadHome();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragmentKind > -1) {
            outState.putInt("fragmentKind", fragmentKind);
        }
    }

    private void loadHome() {
        if (fragmentKind == 0) {
            return;
        }
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
        if (fragmentKind == level) {
            return;
        }
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
