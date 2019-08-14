package com.b2infosoft.moviehits.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.b2infosoft.moviehits.R;

import static com.b2infosoft.moviehits.Useful.UtilityMethod.PERMISSIONS;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.PERMISSION_ALL;
import static com.b2infosoft.moviehits.Useful.UtilityMethod.hasPermissions;

public class SplashActivity extends AppCompatActivity {

    ImageView splashImage;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mContext = SplashActivity.this;


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        }, 6000);



    }



    public void CheckInternetConnection() {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_check_internet_connection);
        dialog.setCancelable(false);

        TextView btn_tryAgain,tvheader;
        tvheader = dialog.findViewById(R.id.tvheader);
        btn_tryAgain = dialog.findViewById(R.id.btn_tryAgain);

        String Internet="Oops!\n" +
                "No Internet";
        tvheader.setText(Internet);
        btn_tryAgain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if (netInfo != null && netInfo.isConnected()) {
                    //
                    dialog.dismiss();


                }
            }
        });


        dialog.show();
    }


}
