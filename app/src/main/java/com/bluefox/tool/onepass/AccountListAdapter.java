package com.bluefox.tool.onepass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluefox.tool.onepass.model.Account;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountListHolder> {
    private List<Account> items;
    private Context context;

    public AccountListAdapter(Context context, List<Account> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AccountListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_account_vertical, parent, false);
        return new AccountListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListHolder holder, int position) {
        Account account = this.items.get(position);
        holder.username.setText(account.UserName);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class AccountListHolder extends RecyclerView.ViewHolder {
        public TextView username;

        public AccountListHolder(View itemView) {
            super(itemView);
            this.username = (TextView)itemView.findViewById(R.id.username);
        }
    }
}
