package com.bluefox.tool.onepass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluefox.tool.onepass.model.Site;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SiteListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SiteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SiteListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_LEVEL = "level";
    private static final String ARG_KEYWORD = "keyword";

    private long id;
    private int level;
    private String keyword;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private SiteListAdapter adapter;

    public SiteListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param level Parameter 2.
     * @param keyword Parameter 3.
     * @return A new instance of fragment SiteListFragment.
     */
    public static SiteListFragment newInstance(long id, int level, String keyword) {
        SiteListFragment fragment = new SiteListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putInt(ARG_LEVEL, level);
        args.putString(ARG_KEYWORD, keyword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
            level = getArguments().getInt(ARG_LEVEL);
            keyword = getArguments().getString(ARG_KEYWORD);
        }
        else {
            id = 0;
            level = 0;
            keyword = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_site_list, container, false);
        //Bind site item list adapter
        try {
            List<Site> siteList = null;
            if (this.id > 0) {
                siteList = Store.getInstance(this.context).getSite(this.id, 0);
            }
            else if (this.level > 0) {
                siteList = Store.getInstance(this.context).getSite(0, this.level);
            }
            else if (this.keyword != null && this.keyword != "") {
                siteList = Store.getInstance(this.context).search(this.keyword, false);
            }
            else {
                throw new Exception("parameter is invalid!");
            }
            this.adapter = new SiteListAdapter(this.context, siteList);
            this.adapter.setOnSiteClickListener(new SiteListAdapter.OnSiteClickListener() {
                @Override
                public void onSiteClick(View view, Site site) {
                    Intent intent = new Intent(context, AccountListActivity.class);
                    intent.putExtra("siteId", site.Id);
                    startActivity(intent);
                }

                @Override
                public void onUrlClick(View view, String url) {
                    Uri uri = Uri.parse(url);
                    Intent intent  = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            RecyclerView items = (RecyclerView)view.findViewById(R.id.items);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.context);
            items.setLayoutManager(layoutManager);
            items.setAdapter(this.adapter);
        } catch (Exception e) {
            e.printStackTrace();
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
