package com.bluefox.tool.onepass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluefox.tool.onepass.model.Account;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_SITEID = "siteId";

    private long id;
    private long siteId;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private AccountListAdapter adapter;

    private ClipboardManager clipboardManager;

    public AccountListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param siteId Parameter 2.
     * @return A new instance of fragment AccountListFragment.
     */
    public static AccountListFragment newInstance(long id, long siteId) {
        AccountListFragment fragment = new AccountListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_SITEID, siteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//获取剪贴板管理器：
        this.clipboardManager = (ClipboardManager)this.context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
            siteId = getArguments().getLong(ARG_SITEID);
        }
        else {
            id = 0;
            siteId = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            List<Account> accountList = null;
            if (this.id > 0) {
                accountList = Store.getInstance(this.context).getAccount(this.id, 0);
            } else if (this.siteId > 0) {
                accountList = Store.getInstance(this.context).getAccount(0, this.siteId);
            } else {
                throw new Exception("parameter is invalid!");
            }
            if (accountList.size() == 0) {
                view = inflater.inflate(R.layout.fragment_no_result, container, false);
            }
            else {
                view = inflater.inflate(R.layout.fragment_account_list, container, false);
                // Decrypt password
                opApp app = (opApp) this.context.getApplicationContext();
                String auth = app.getAuthority();
                for (int i = 0; i < accountList.size(); ++i) {
                    Account acc = accountList.get(i);
                    acc.Password = Aes.decrypt(auth, acc.Password);
                }

                this.adapter = new AccountListAdapter(this.context, accountList);
                this.adapter.setOnAccountClickListener(new AccountListAdapter.OnAccountClickListener() {
                    @Override
                    public void onCopyUserNameClick(View view, String username) {
                        ClipData data = ClipData.newPlainText("temp", username);
                        clipboardManager.setPrimaryClip(data);
                        Toast.makeText(context, "copied successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCopyPasswordClick(View view, String password) {
                        ClipData data = ClipData.newPlainText("temp", password);
                        clipboardManager.setPrimaryClip(data);
                        Toast.makeText(context, "copied successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
                items.setLayoutManager(layoutManager);
                items.setAdapter(this.adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            view = inflater.inflate(R.layout.fragment_no_result, container, false);
        }
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
