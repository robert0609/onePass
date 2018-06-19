package com.bluefox.tool.onepass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiBroad extends BroadcastReceiver {
    private OnWifiStatusListener onWifiStatusListener;

    public void setOnWifiStatusListener(OnWifiStatusListener listener) {
        this.onWifiStatusListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            Log.i("wifiStatus","changed");
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo.State networkState = info.getState();
            if (networkState.equals(NetworkInfo.State.DISCONNECTED)) {
                // 如果断开连接
                Log.i("wifiStatus","disconnected wifi");
                if (this.onWifiStatusListener != null) {
                    this.onWifiStatusListener.onDisconnected();
                }
            }
            else if(networkState.equals(NetworkInfo.State.CONNECTED)){
                Log.i("wifiStatus","connected wifi");
                if (this.onWifiStatusListener != null) {
                    this.onWifiStatusListener.onConnected();
                }
            }
        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            // WIFI开关
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                // 如果关闭
                Log.i("wifiStatus","switch off wifi");
                if (this.onWifiStatusListener != null) {
                    this.onWifiStatusListener.onSwitchOff();
                }
            }
            else if(wifistate==WifiManager.WIFI_STATE_ENABLED){
                Log.i("wifiStatus","switch on wifi");
                if (this.onWifiStatusListener != null) {
                    this.onWifiStatusListener.onSwitchOn();
                }
            }
        }
    }

    public interface OnWifiStatusListener {
        void onConnected();

        void onDisconnected();

        void onSwitchOff();

        void onSwitchOn();
    }
}
