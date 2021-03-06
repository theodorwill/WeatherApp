package com.example.cba.TheoVaderApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cba.TheoVaderApp.data.Channel;
import com.example.cba.TheoVaderApp.data.Item;
import com.example.cba.TheoVaderApp.service.WeatherServiceCallback;
import com.example.cba.TheoVaderApp.service.YahooWeatherService;
import java.sql.Time;
import java.util.Date;

public class WeatherList extends Activity implements WeatherServiceCallback, AdapterView.OnItemSelectedListener{

    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private TextView dateTextView;
    private Button refreshCity;
    private Spinner citySelection;
    private ImageView weatherIconImageView;
    private final int cityA = 0;
    private final int cityB = 1;
    private final int cityC = 2;
    private final int cityD = 3;
    private int currentCity = 0;
    private final Handler handler = new Handler();
    private RelativeLayout currLay;
    private YahooWeatherService service;
    private ProgressDialog pDialog;
    private Toast errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherview);

        weatherIconImageView = (ImageView)findViewById(R.id.weatherImage);
        temperatureTextView = (TextView)findViewById(R.id.temperatureText);
        conditionTextView = (TextView)findViewById(R.id.conditionText);
        locationTextView = (TextView)findViewById(R.id.locationText);
        dateTextView = (TextView)findViewById(R.id.dateView);
        citySelection = (Spinner)findViewById(R.id.cityName);
        refreshCity = (Button)findViewById(R.id.refreshButton);
        currLay = (RelativeLayout)findViewById(R.id.relLay);
        service = new YahooWeatherService(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading...");
        pDialog.show();

        errorToast = Toast.makeText(getApplicationContext(),
                "Connection failed.", Toast.LENGTH_SHORT);

        service.refreshWeather("Stockholm, Sweden");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cityName,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citySelection.setAdapter(adapter);
        citySelection.setOnItemSelectedListener(this);

        refreshCity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pDialog.show();
                refreshCurrent();
            }
        });
    }

    @Override
    public void serviceSuccess(Channel channel) {

        pDialog.hide();
        errorToast.cancel();

        int tsInt = new Time(System.currentTimeMillis()).getHours();
        String ctString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        dateTextView.setText("Last updated: " + ctString);

        Item item = channel.getItem();

        int resourceId = getResources().getIdentifier("drawable/icon_" +
                 channel.getItem().getCondition().getCode(), null, getPackageName());

        int temp = item.getCondition().getTemperature();

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);

        temperatureTextView.setText(item.getCondition().getTemperature()+"\u00B0"+
                        channel.getUnits().getTemperature());

        conditionTextView.setText(item.getCondition().getDescription());

        locationTextView.setText(service.getLocation());

        if (temp >= 10 && tsInt < 19 && tsInt > 5){
            currLay.setBackgroundColor(Color.rgb(255, 152, 0));
        }
        else if (temp < 10 && tsInt < 19 && tsInt > 5){
            currLay.setBackgroundColor(Color.rgb(3, 169, 244));
        }
        else if (tsInt >= 19 || tsInt <= 5){
            currLay.setBackgroundColor(Color.rgb(63, 81, 181));
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        networkCheck();
        return;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(position){
            case cityA:
                pDialog.setMessage("Loading...");
                pDialog.show();
                service.refreshWeather("Stockholm, Sweden");
                currentCity = 0;
                break;
            case cityB:
                pDialog.setMessage("Loading...");
                pDialog.show();
                service.refreshWeather("Gothenburg, Sweden");
                currentCity = 1;
                break;
            case cityC:
                pDialog.setMessage("Loading...");
                pDialog.show();
                service.refreshWeather("Malmo, Sweden");
                currentCity = 2;
                break;
            case cityD:
                pDialog.setMessage("Loading...");
                pDialog.show();
                service.refreshWeather("Kiruna, Sweden");
                currentCity = 3;
                break;
        }
    }

    public void refreshCurrent(){
        if (currentCity == 0){
            pDialog.setMessage("Loading...");
            pDialog.show();
            service.refreshWeather("Stockholm, Sweden");
        }
        else if (currentCity == 1){
            pDialog.setMessage("Loading...");
            pDialog.show();
            service.refreshWeather("Gothenburg, Sweden");
        }
        else if (currentCity == 2){
            pDialog.setMessage("Loading...");
            pDialog.show();
            service.refreshWeather("Malmo, Sweden");
        }
        else if (currentCity == 3){
            pDialog.setMessage("Loading...");
            pDialog.show();
            service.refreshWeather("Kiruna, Sweden");
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo !=null;
    }

    private void networkAlert(){
        AlertDialog.Builder checkWindow = new  AlertDialog.Builder(WeatherList.this);

        checkWindow.setTitle("Error!");
        checkWindow.setMessage("No internet Connection found!");
        checkWindow.setCancelable(false);

        checkWindow.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                refreshCurrent();
            }
        });

        checkWindow.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        checkWindow.show();
    }

    private void networkCheck(){
        if(isNetworkAvailable()){
            return;
        }else if(!isNetworkAvailable()){
            networkAlert();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }


}
