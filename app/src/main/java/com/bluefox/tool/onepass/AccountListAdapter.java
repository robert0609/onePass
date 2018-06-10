package com.bluefox.tool.onepass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bluefox.tool.onepass.model.Account;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountListHolder> {
    private List<Account> items;
    private Context context;
    private OnAccountClickListener onAccountClickListener;

    public AccountListAdapter(Context context, List<Account> items) {
        this.items = items;
        this.context = context;
    }

    public void setOnAccountClickListener(OnAccountClickListener listener) {
        this.onAccountClickListener = listener;
    }

    @NonNull
    @Override
    public AccountListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_account_vertical, parent, false);
        return new AccountListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListHolder holder, int position) {
        final Account account = this.items.get(position);
        holder.username.setText(account.UserName);

        if (this.onAccountClickListener != null) {
            holder.copyUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAccountClickListener.onCopyUserNameClick(v, account.UserName);
                }
            });

            holder.copyPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAccountClickListener.onCopyPasswordClick(v, account.Password);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class AccountListHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public Button copyUserName;
        public Button copyPassword;

        public AccountListHolder(View itemView) {
            super(itemView);
            this.username = (TextView)itemView.findViewById(R.id.username);
            this.copyUserName = (Button)itemView.findViewById(R.id.copy_username);
            this.copyPassword = (Button)itemView.findViewById(R.id.copy_password);
        }
    }

    public interface OnAccountClickListener {
        void onCopyUserNameClick(View view, String username);

        void onCopyPasswordClick(View view, String password);
    }
}
