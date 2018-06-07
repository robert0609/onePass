package com.bluefox.tool.onepass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluefox.tool.onepass.model.Site;

import java.util.List;

public class SiteListAdapter extends RecyclerView.Adapter<SiteListAdapter.SiteListHolder> {
    private List<Site> items;
    private Context context;

    public SiteListAdapter(Context context, List<Site> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SiteListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_site_vertical, parent, false);
        return new SiteListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteListHolder holder, int position) {
        Site item = this.items.get(position);
        holder.siteName.setText(item.Name);
        holder.url.setText(item.Url);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class SiteListHolder extends RecyclerView.ViewHolder {
        public TextView siteName;
        public TextView url;

        public SiteListHolder(View itemView) {
            super(itemView);
            this.siteName = (TextView)itemView.findViewById(R.id.siteName);
            this.url = (TextView)itemView.findViewById(R.id.url);
        }
    }
}
