package com.example.vehicle_breakdown_help_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.narify.netdetect.NetDetect;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.narify.netdetect.NetDetect.*;

public class splashscreen_activity extends AppCompatActivity{
BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);
        /*NetDetect.init(this);*/
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splashscreen_activity.this,Login_activity.class));
                finish();

            }
        },3000);
       /* check_permissions();*/
        broadcastReceiver = new Connectivity_Reciver();
        registernetwork();
/*        getSupportActionBar().hide();*/
    }

    public void check_permissions()
    {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if(multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            Toast.makeText(splashscreen_activity.this, "Permissions Granted!", Toast.LENGTH_SHORT).show();
                            NetDetect.check(new ConnectivityCallback() {
                               @Override
                               public void onDetected(boolean isConnected) {
                                   if (isConnected == true)
                                   {
                                      /* Toast.makeText(splashscreen_activity.this, "Internet is connected", Toast.LENGTH_LONG).show();*/

                                       Handler handler =new Handler();
                                       handler.postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                               startActivity(new Intent(splashscreen_activity.this,Login_activity.class));
                                               finish();

                                           }
                                       },3000);
                                   }
                                   else if (isConnected ==false)
                                   {
                                       /*Toast.makeText(splashscreen_activity.this, "Please Connect Internet", Toast.LENGTH_LONG).show();*/
                                   }
                               }
                           });

                        }
                        if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied())
                        {
                            Toast.makeText(splashscreen_activity.this, "Permissions NOT Granted!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
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