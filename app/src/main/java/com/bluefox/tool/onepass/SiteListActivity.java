package com.bluefox.tool.onepass;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class SiteListActivity extends BaseActivity implements SiteListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_site_list);

        //init site list fragment
        SiteListFragment siteListFragment = SiteListFragment.newInstance(0, 1, null);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.siteListLayout, siteListFragment);
        fragmentTransaction.commit();

        HttpServer server = new HttpServer(this);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
