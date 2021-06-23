package com.example.vehicle_breakdown_help_app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class check_con {

    public boolean CheckInternet(Context context) {


            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (null != networkInfo) {
            /*if (networkInfo.getType()==ConnectivityManager.TYPE_WIFI)
            {
                Toast.makeText(context, "wifi", Toast.LENGTH_SHORT).show();
            }
            else if(networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)
            {
                Toast.makeText(context, "mobile", Toast.LENGTH_SHORT).show();
            }*/
                return true;
            } else {
                return false;
            }

    }
}
