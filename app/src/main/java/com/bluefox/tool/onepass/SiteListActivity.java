package com.bluefox.tool.onepass;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class SiteListActivity extends DrawerActivity implements SiteListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_site_list);

        Intent intent = this.getIntent();
        int level = intent.getIntExtra("level", 0);

        this.loadSiteList(level);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int level = intent.getIntExtra("level", 0);

        this.loadSiteList(level);
    }

    private void loadSiteList(int level) {

        //init site list fragment
        SiteListFragment siteListFragment = SiteListFragment.newInstance(0, level, null);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.siteListLayout, siteListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
