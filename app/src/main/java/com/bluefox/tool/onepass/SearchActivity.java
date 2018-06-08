package com.bluefox.tool.onepass;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;

public class SearchActivity extends AppCompatActivity implements SiteListFragment.OnFragmentInteractionListener {

    private SearchView searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.searchInput = (SearchView)findViewById(R.id.input_keyword);
        this.searchInput.setIconified(false);
        this.searchInput.onActionViewExpanded();
        this.searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void search(String keyword) {
        //init site list fragment
        SiteListFragment siteListFragment = SiteListFragment.newInstance(0, 0, keyword);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.search_result, siteListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
