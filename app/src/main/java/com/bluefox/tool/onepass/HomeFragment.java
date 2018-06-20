package com.bluefox.tool.onepass;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
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

    private WifiBroad wifiBroad;

    private boolean isCreatedView = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("HomeFragment: ", "onCreate");
        this.wifiBroad = new WifiBroad();
        this.wifiBroad.setOnWifiStatusListener(new WifiBroad.OnWifiStatusListener() {
            @Override
            public void onConnected() {
                if (isCreatedView) {
                    //刚连接上wifi的瞬间，无法获取到IP，故等待500毫秒之后获取
                    (new WifiConnectedTask()).execute();
                }
            }

            @Override
            public void onDisconnected() {
                if (isCreatedView) {
                    wifiDisconnected();
                }
            }

            @Override
            public void onSwitchOff() {

            }

            @Override
            public void onSwitchOn() {

            }
        });
        IntentFilter filter=new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(this.wifiBroad, filter);
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

        isCreatedView = true;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("HomeFragment: ", "onStart");
        this.init();
    }

    private void init() {
        Net net = new Net(this.context);
        String address = net.getLocalIpAddress();
        if (address == null) {
            this.wifiDisconnected();
        }
        else {
            this.wifiConnected();
        }
    }

    private void wifiConnected() {
        opApp app = (opApp) this.context.getApplicationContext();
        Net net = new Net(this.context);
        String address = net.getLocalIpAddress();
        wifiOff.setVisibility(View.INVISIBLE);
        webToggle.setVisibility(View.VISIBLE);
        webToggle.setChecked(app.getHttpIsStart());
        webUrl.setText("http://" + address + ":18888");
    }

    private void wifiDisconnected() {
        opApp app = (opApp) this.context.getApplicationContext();
        webToggle.setVisibility(View.INVISIBLE);
        wifiOff.setVisibility(View.VISIBLE);
        app.stopHttpServer();
        webUrl.setText(R.string.wifi_off_tips);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreatedView = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.wifiBroad);
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

    private class WifiConnectedTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            wifiConnected();
        }
    }
}
