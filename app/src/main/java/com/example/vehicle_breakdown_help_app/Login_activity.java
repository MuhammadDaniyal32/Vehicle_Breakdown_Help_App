package com.example.vehicle_breakdown_help_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Login_activity extends AppCompatActivity {
    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);
        broadcastReceiver = new Connectivity_Reciver();
        registernetwork();
        userlogin_frag fragment = new userlogin_frag();
        getSupportFragmentManager().beginTransaction()
           .add(R.id.frag,fragment).commit();
    }
    protected void registernetwork()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisternetwork()
    {
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisternetwork();
    }
}