package com.example.cba.TheoVaderApp.service;

import com.example.cba.TheoVaderApp.data.Channel;

/**
 * Created by cba on 2017-05-08.
 */

public interface WeatherServiceCallback {

    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
