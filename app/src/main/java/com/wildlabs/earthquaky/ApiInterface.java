package com.wildlabs.earthquaky;

import com.wildlabs.earthquaky.models.EarthquakeData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson")
    Call<EarthquakeData> getTodos();

}
