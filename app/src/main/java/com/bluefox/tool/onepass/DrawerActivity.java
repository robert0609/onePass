package com.bluefox.tool.onepass;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AuthActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView drawerLeftMenu;
    private LinearLayout commonContent;

    private OnDrawerMenuClickListener onDrawerMenuClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_drawer);
        this.toolbar = (Toolbar)findViewById(R.id.common_toolbar);
        setSupportActionBar(this.toolbar);
        this.setBackArrow();
        this.commonContent = (LinearLayout)findViewById(R.id.common_content);
        this.drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        this.drawerLeftMenu = (NavigationView)findViewById(R.id.drawer_left_menu);

        this.drawerLeftMenu.setNavigationItemSelectedListener(this);

        View drawerHeader = this.drawerLeftMenu.getHeaderView(0);
        TextView txtVersion = (TextView)drawerHeader.findViewById(R.id.drawer_header_version);
        txtVersion.setText("Version: " + getVersionName());
    }

    /**
     * 设置左上角back按钮
     */
    public void setBackArrow() {
        final Drawable drawableMenu = getResources().getDrawable(R.drawable.ic_drawer_menu_white_24dp);
        //给ToolBar设置左侧的图标
        getSupportActionBar().setHomeAsUpIndicator(drawableMenu);
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回按钮的点击事件
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerLeftMenu);
            }
        });
    }

    public void setContentLayout(int layoutId) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View contentView = layoutInflater.inflate(layoutId, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.commonContent.addView(contentView, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (this.onDrawerMenuClickListener != null) {
            int id = item.getItemId();
            switch (id) {
                case R.id.drawer_item_home:
                    onDrawerMenuClickListener.onNavigateToHome();
                    break;
                case R.id.drawer_item_level_1:
                    onDrawerMenuClickListener.onNavigateToSiteList(1);
                    break;
                case R.id.drawer_item_level_2:
                    onDrawerMenuClickListener.onNavigateToSiteList(2);
                    break;
                case R.id.drawer_item_level_3:
                    onDrawerMenuClickListener.onNavigateToSiteList(3);
                    break;
            }
        }
        this.drawerLayout.closeDrawer(this.drawerLeftMenu);
        return true;
    }

    public void setOnDrawerMenuClickListener(OnDrawerMenuClickListener listener) {
        this.onDrawerMenuClickListener = listener;
    }

    private String getVersionName()
    {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    public interface OnDrawerMenuClickListener {
        void onNavigateToHome();

        void onNavigateToSiteList(int level);
    }
}
