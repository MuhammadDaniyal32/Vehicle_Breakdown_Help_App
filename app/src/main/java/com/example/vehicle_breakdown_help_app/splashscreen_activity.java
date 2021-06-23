package com.example.vehicle_breakdown_help_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.List;

public class splashscreen_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);
        check_permissions();
        getSupportActionBar().hide();
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

                            check_con con =new check_con();
                            con.CheckInternet(splashscreen_activity.this);

                            if (con.CheckInternet(splashscreen_activity.this)==true)
                            {
                                Toast.makeText(splashscreen_activity.this, "Internet is connected", Toast.LENGTH_SHORT).show();

                                Handler handler =new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(splashscreen_activity.this,Login_activity.class));
                                        finish();

                                    }
                                },3000);
                            }
                            if (con.CheckInternet(splashscreen_activity.this)==false)
                            {
                                Toast.makeText(splashscreen_activity.this, "Please Connect Internet", Toast.LENGTH_SHORT).show();

                            }
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
}