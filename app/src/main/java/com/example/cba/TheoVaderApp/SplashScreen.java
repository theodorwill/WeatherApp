package com.example.cba.TheoVaderApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;


public class SplashScreen extends Activity {

    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(!isNetworkAvailable()){

            AlertDialog.Builder checkBuilder = new  AlertDialog.Builder(SplashScreen.this);

            checkBuilder.setTitle("Error!");
            checkBuilder.setMessage("No internet Connection found!");
            checkBuilder.setCancelable(false);

            checkBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //Restart The Activity
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            });

            checkBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }) ;

            AlertDialog alert = checkBuilder.create();
            alert.show();
            progressBar.setVisibility(View.GONE);

        }
        else {
            if (isNetworkAvailable()){

                Thread tr=new Thread(){
                    public  void  run(){
                        try {
                            sleep(3000);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            startActivity(new Intent(SplashScreen.this,WeatherList.class));
                            finish();
                        }
                    }
                };
                tr.start();
            }
        }

    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null;
    }
}

