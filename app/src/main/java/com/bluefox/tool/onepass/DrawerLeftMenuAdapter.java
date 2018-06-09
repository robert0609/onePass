package com.bluefox.tool.onepass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DrawerLeftMenuAdapter extends BaseAdapter {
    private List<String> menuList;
    private Context context;

    public DrawerLeftMenuAdapter(Context context, List<String> menuList) {
        this.menuList = menuList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_drawer_left, parent,false);
            holder = new ViewHolder();
            holder.txtMenuItem = (TextView)convertView.findViewById(R.id.drawer_item);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.txtMenuItem.setText(this.menuList.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView txtMenuItem;
    }
}
