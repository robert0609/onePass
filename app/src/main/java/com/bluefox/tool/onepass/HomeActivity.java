package com.bluefox.tool.onepass;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HomeActivity extends DrawerActivity implements CompoundButton.OnCheckedChangeListener {
    private ToggleButton webToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_home);
        opApp app = (opApp)getApplicationContext();
        webToggle = (ToggleButton)findViewById(R.id.web_toggle);
        webToggle.setChecked(app.getHttpIsStart());
        webToggle.setOnCheckedChangeListener(this);

        TextView webUrl = (TextView)findViewById(R.id.management_url);
        webUrl.setText("http://" + this.getLocalIpAddress() + ":18888");
    }

    @Override
    protected void onResume() {
        super.onResume();
        opApp app = (opApp)getApplicationContext();
        webToggle.setChecked(app.getHttpIsStart());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public String getLocalIpAddress() {
        Context context = getApplicationContext();
        NetworkInfo info = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                //当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    return intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                }
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        opApp app = (opApp)getApplicationContext();
        if (isChecked) {
            if (!app.startHttpServer()) {
                webToggle.setChecked(false);
                Toast.makeText(this, "Http server start failed!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            app.stopHttpServer();
        }
    }
}
