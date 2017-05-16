package com.example.cba.TheoVaderApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.DateFormat;
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

import java.util.Calendar;
import java.util.Date;

public class WeatherList extends Activity implements WeatherServiceCallback, AdapterView.OnItemSelectedListener{

    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private Button refreshCity;
    private Spinner citySelection;
    private ImageView weatherIconImageView;
    private final int cityA = 0;
    private final int cityB = 1;
    private final int cityC = 2;
    private final int cityD = 3;
    private final Handler handler = new Handler();
    private RelativeLayout currLay;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherview);

        weatherIconImageView = (ImageView)findViewById(R.id.weatherImage);
        temperatureTextView = (TextView)findViewById(R.id.temperatureText);
        conditionTextView = (TextView)findViewById(R.id.conditionText);
        locationTextView = (TextView)findViewById(R.id.locationText);
        citySelection = (Spinner)findViewById(R.id.cityName);
        refreshCity = (Button)findViewById(R.id.refreshButton);
        currLay = (RelativeLayout)findViewById(R.id.relLay);
        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        service.refreshWeather("Stockholm, Sweden");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cityName,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citySelection.setAdapter(adapter);
        citySelection.setOnItemSelectedListener(this);

        refreshCity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Long tsLong = System.currentTimeMillis()/(1000*60*60)%24;

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

        if (temp >= 10 && tsLong < 19){
            currLay.setBackgroundColor(Color.rgb(255, 193, 7));
        }
        else if (temp < 10 && tsLong < 19){
            currLay.setBackgroundColor(Color.rgb(3, 169, 244));
        }
        else if (tsLong >= 19 && tsLong <= 5){
            currLay.setBackgroundColor(Color.rgb(63, 81, 181));
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        dialog.hide();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(position){
            case cityA:
                dialog.setMessage("Loading...");
                dialog.show();
                service.refreshWeather("Stockholm, Sweden");
                break;
            case cityB:
                dialog.setMessage("Loading...");
                dialog.show();
                service.refreshWeather("Gothenburg, Sweden");
                break;
            case cityC:
                dialog.setMessage("Loading...");
                dialog.show();
                service.refreshWeather("Malmo, Sweden");
                break;
            case cityD:
                dialog.setMessage("Loading...");
                dialog.show();
                service.refreshWeather("Kiruna, Sweden");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }
}
