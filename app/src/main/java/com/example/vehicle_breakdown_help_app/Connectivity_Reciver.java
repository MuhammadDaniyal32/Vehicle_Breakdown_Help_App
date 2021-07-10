package com.example.vehicle_breakdown_help_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.narify.netdetect.NetDetect;

public class Connectivity_Reciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NetDetect.init(context);
        NetDetect.check(new NetDetect.ConnectivityCallback() {

            @Override
            public void onDetected(boolean isConnected) {
                if (isConnected == true)
                {
                    Toast.makeText(context, "Internet Is Connected", Toast.LENGTH_LONG).show();
                }
                else if (isConnected ==false)
                {
                    Toast.makeText(context, "Please Connect Internet!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}