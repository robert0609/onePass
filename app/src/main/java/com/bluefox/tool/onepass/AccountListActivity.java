package com.bluefox.tool.onepass;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

public class AccountListActivity extends BaseActivity implements AccountListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_account_list);

        Intent intent = this.getIntent();
        long siteId = intent.getLongExtra("siteId", 0);

        //init site list fragment
        AccountListFragment accountListFragment = AccountListFragment.newInstance(0, siteId);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.accountListLayout, accountListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
