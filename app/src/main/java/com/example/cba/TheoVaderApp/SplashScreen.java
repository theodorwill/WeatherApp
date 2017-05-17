package com.example.cba.TheoVaderApp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;


public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    private boolean InternetCheck=true;
    private ProgressBar spinner;
    AlertDialog.Builder internetAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        //progressBar
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        internetAlert = new AlertDialog.Builder(this);
        PostDelayedMethod();

    }


    public void PostDelayedMethod()
    {

        new Handler().postDelayed(new Runnable() {


            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                // This method will be executed once the timer is over
                // Start your app main activity

                boolean InternetResult = checkConnection();
                if(InternetResult){

                    //open Activity when internet is connected
                    Intent intent=new Intent(SplashScreen.this,WeatherList.class);
                    //     intent.addCategory(Intent.CATEGORY_HOME);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
                else {
                    spinner.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);


                    //Dialog Box show when internet is not connected
                    DialogAppear();


                }
            }
        }, SPLASH_TIME_OUT);
    }


    //DialogBox Main Function
    public void DialogAppear()
    {
        AlertDialog.Builder meh = new AlertDialog.Builder(SplashScreen.this);

        meh.setTitle("Network Error");   //Title
        meh.setMessage("No Internet Connectivity");   //Message

        meh.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Check internet again when click on Retry by calling function

                        //run is not working there due to runnable method
                        // run();
                        PostDelayedMethod();

                    }
                });


        //Negative Message
        meh.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                                    /* close this activity
                                    *  When Exit is clicked
                                    */
                        finish();

                    }
                });

        showDialog();
    }

    public void showDialog() {
        internetAlert.show();
    }


    //Check Internet status of the mobile
    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    //Return Internet Status of the Mobile
    public boolean checkConnection(){
        if(isOnline()){
            return InternetCheck;
            //Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            InternetCheck=false;
            return InternetCheck;
            // Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();

        }

    }
}

