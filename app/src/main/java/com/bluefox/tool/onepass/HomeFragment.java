package com.bluefox.tool.onepass;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Context context;

    private ToggleButton webToggle;
    private ImageView wifiOff;
    private TextView webUrl;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("HomeFragment: ", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("HomeFragment: ", "onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        webToggle = (ToggleButton)view.findViewById(R.id.web_toggle);
        webUrl = (TextView)view.findViewById(R.id.management_url);
        wifiOff = (ImageView)view.findViewById(R.id.wifi_off);
        webToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                opApp app = (opApp)context.getApplicationContext();
                if (isChecked) {
                    if (!app.startHttpServer()) {
                        webToggle.setChecked(false);
                        Toast.makeText(context, "Http server start failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    app.stopHttpServer();
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("HomeFragment: ", "onStart");
        this.init();
    }

    private void init() {
        opApp app = (opApp)this.context.getApplicationContext();
        Net net = new Net(this.context);
        String address = net.getLocalIpAddress();
        if (address == null) {
            webToggle.setVisibility(View.INVISIBLE);
            wifiOff.setVisibility(View.VISIBLE);
            app.stopHttpServer();
            webUrl.setText(R.string.wifi_off_tips);
        }
        else {
            wifiOff.setVisibility(View.INVISIBLE);
            webToggle.setVisibility(View.VISIBLE);
            webToggle.setChecked(app.getHttpIsStart());
            webUrl.setText("http://" + address + ":18888");
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("HomeFragment: ", "onAttach");
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
        Log.i("HomeFragment: ", "onDetach");
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
