package com.bluefox.tool.onepass;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AuthActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ListView drawerLeftMenu;
    private LinearLayout commonContent;

    private List<String> menus;
    private String titleHome;
    private String titleLevel1;
    private String titleLevel2;
    private String titleLevel3;

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
        this.drawerLeftMenu = (ListView)findViewById(R.id.drawer_left_menu);


        this.titleHome = getResources().getText(R.string.home).toString();
        this.titleLevel1 = getResources().getText(R.string.level_1).toString();
        this.titleLevel2 = getResources().getText(R.string.level_2).toString();
        this.titleLevel3 = getResources().getText(R.string.level_3).toString();

        this.menus = new ArrayList<>();
        this.menus.add(titleHome);
        this.menus.add(titleLevel1);
        this.menus.add(titleLevel2);
        this.menus.add(titleLevel3);
        DrawerLeftMenuAdapter drawerLeftMenuAdapter = new DrawerLeftMenuAdapter(this, this.menus);
        this.drawerLeftMenu.setAdapter(drawerLeftMenuAdapter);
        this.drawerLeftMenu.setOnItemClickListener(this);
    }

    /**
     * 设置左上角back按钮
     */
    public void setBackArrow() {
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String menuTitle = this.menus.get(position);
        if (menuTitle == titleLevel1) {
            Intent intent = new Intent(this, SiteListActivity.class);
            intent.putExtra("level", 1);
            startActivity(intent);
        }
        else if (menuTitle == titleLevel2) {
            Intent intent = new Intent(this, SiteListActivity.class);
            intent.putExtra("level", 2);
            startActivity(intent);
        }
        else if (menuTitle == titleLevel3) {
            Intent intent = new Intent(this, SiteListActivity.class);
            intent.putExtra("level", 3);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        this.drawerLayout.closeDrawer(this.drawerLeftMenu);
    }
}
