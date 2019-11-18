package com.liuzhongqiang.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ConfigBroadcast broadcast;
    private static final String TAG = "zzz";

    MyGridView gridView;
    private Context mContext;
    private WifiStateBroadcastReceiver wifiStateBroadcastReceiver;
    private H handler = new H();
    private static final int WIFI_STATE_CHANGED = 1;
    private String[] wifiState = {"正在关闭", "关闭", "正在开启", "开启", "未知"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scoll);
        gridView = findViewById(R.id.app_list);
        AppGridAdapter adapter = new AppGridAdapter(this);
        gridView.setAdapter(adapter);
        mContext = this;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiStateBroadcastReceiver = new WifiStateBroadcastReceiver();
        mContext.registerReceiver(wifiStateBroadcastReceiver, intentFilter);


    }

    class ConfigBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String reason = intent.getStringExtra("reason");
            Log.i(TAG, "reason: " + reason);
        }
    }

    @Override
    protected void onDestroy() {

        if (broadcast != null) {
            this.unregisterReceiver(broadcast);
        }
        if (wifiStateBroadcastReceiver != null) {
            this.unregisterReceiver(wifiStateBroadcastReceiver);
        }
        super.onDestroy();
    }


    class WifiStateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                handler.obtainMessage(WIFI_STATE_CHANGED, state).sendToTarget();
//                switch (state) {
//                    case WifiManager.WIFI_STATE_DISABLING:
//                        Log.i(TAG, "onReceive: disabling");
//                        break;
//                    case WifiManager.WIFI_STATE_DISABLED:
//                        Log.i(TAG, "onReceive: disabled");
//                        break;
//                    case WifiManager.WIFI_STATE_ENABLING:
//                        Log.i(TAG, "onReceive: enabling");
//                        break;
//                    case WifiManager.WIFI_STATE_ENABLED:
//                        Log.i(TAG, "onReceive: enabled");
//                        getWifiInfo();
//                        break;
//                    case WifiManager.WIFI_STATE_UNKNOWN:
//                        Log.i(TAG, "onReceive: unknown");
//                        break;
//                    default:
//                        break;
//                }
            }
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {

            }
        }
    }
    public String getWifiInfo() {

        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                String wifiName = networkInfo.getExtraInfo();
                StringBuilder rewifiInfo = new StringBuilder(wifiName);
                Log.i(TAG, "getWifiInfo: " + wifiName);
                WifiManager wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
                int state = wifiManager.getWifiState();
                if (state == WifiManager.WIFI_STATE_ENABLED) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    rewifiInfo.append(",").append(int2ip(wifiInfo.getIpAddress()));
                    Log.i(TAG, "getWifiInfo: ip: " + int2ip(wifiInfo.getIpAddress()));
                }
                return rewifiInfo.toString();
            }
        }
        return null;
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    protected final class H extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WIFI_STATE_CHANGED) {
                Log.i(TAG, "handleMessage: "+ wifiState[(int)msg.obj]);
                if ((int)msg.obj == WifiManager.WIFI_STATE_ENABLED) {
                    getWifiInfo();
                }
            } else if (msg.what == 2) {

            } else {
                throw new IllegalArgumentException("Unknown msg: " + msg.what);
            }
        }
    }


}
