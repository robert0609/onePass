package com.bluefox.tool.onepass;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SearchActivity extends AuthActivity implements SiteListFragment.OnFragmentInteractionListener {
    private Toolbar toolbar;

    private SearchView searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(this.toolbar);
        this.setBackArrow();
    }

    /**
     * 设置左上角back按钮
     */
    public void setBackArrow() {
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        //给ToolBar设置左侧的图标
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回按钮的点击事件
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchButton = menu.findItem(R.id.app_bar_input_search);
        this.searchInput = (SearchView)MenuItemCompat.getActionView(searchButton);
        this.searchInput.setIconified(false);
        this.searchInput.onActionViewExpanded();
        this.searchInput.setQueryHint("search...");
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
        return true;
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
