package com.bluefox.tool.onepass;

import android.content.Context;
import android.os.IInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluefox.tool.onepass.model.Site;

import java.util.List;

public class SiteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int normalType = 0;
    private int loadMoreType = 1;

    private List<Site> items;
    private boolean hasMore;
    private Context context;
    private OnSiteClickListener onSiteClickListener;

    public SiteListAdapter(Context context, Store.SitePageList itemPageList) {
        this.context = context;
        this.items = itemPageList.SiteList;
        this.hasMore = itemPageList.getHasMore();
    }

    public void setOnSiteClickListener(OnSiteClickListener listener) {
        this.onSiteClickListener = listener;
    }

    public void update(Store.SitePageList itemPageList) {
        this.items.addAll(itemPageList.SiteList);
        this.hasMore = itemPageList.getHasMore();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("RecycleView Adaper", "getItemViewType position: " + position);
        if (position == getItemCount() - 1) {
            return loadMoreType;
        }
        else {
            return normalType;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("RecycleView Adaper", "onCreateViewHolder viewType: " + viewType);
        if (viewType == loadMoreType) {
            View view = LayoutInflater.from(this.context).inflate(R.layout.item_load_more, parent, false);
            return new LoadMoreHolder(view);
        }
        else {
            View view = LayoutInflater.from(this.context).inflate(R.layout.item_site_vertical, parent, false);
            return new SiteListHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreHolder) {
            Log.d("RecycleView Adaper", "onBindViewHolder LoadMoreHolder position: " + position);
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder)holder;
            if (hasMore) {
                loadMoreHolder.loadMore.setText(R.string.pull_up_load_more);
            }
            else {
                loadMoreHolder.loadMore.setText(R.string.no_more);
            }
        }
        else {
            Log.d("RecycleView Adaper", "onBindViewHolder SiteListHolder position: " + position);
            SiteListHolder normalHolder = (SiteListHolder)holder;
            final Site item = this.items.get(position);
            normalHolder.siteName.setText(item.Name);
            final String url = item.Url.equals("none") ? "" : item.Url;
            normalHolder.url.setText(url);

            if (this.onSiteClickListener != null) {
                normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSiteClickListener.onSiteClick(v, item);
                    }
                });

                normalHolder.url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSiteClickListener.onUrlClick(v, url);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size() + 1;
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

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        public TextView loadMore;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            this.loadMore = (TextView)itemView.findViewById(R.id.loadMore);
        }
    }

    public interface OnSiteClickListener {
        void onSiteClick(View view, Site site);

        void onUrlClick(View view, String url);
    }
}
