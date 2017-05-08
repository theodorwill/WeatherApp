package com.example.cba.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cba.weatherapp.data.Channel;
import com.example.cba.weatherapp.data.Item;
import com.example.cba.weatherapp.service.WeatherServiceCallback;
import com.example.cba.weatherapp.service.YahooWeatherService;

public class WeatherList extends Activity implements WeatherServiceCallback {

    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherview);

        temperatureTextView = (TextView)findViewById(R.id.temperatureText);
        conditionTextView = (TextView)findViewById(R.id.conditionText);
        locationTextView = (TextView)findViewById(R.id.locationText);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        service.refreshWeather("Stockholm, Sweden");
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();

        int resource = getResources().getIdentifier("drawable/icon" + channel.getItem().getCondition().getCode(), null, getPackageName());


                temperatureTextView.setText(item.getCondition().getTemperature()+"\u00B0"+ channel.getUnits().getTemperature());

                conditionTextView.setText(item.getCondition().getDescription());

                locationTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
