package com.example.rwado.financialcalc.Remote;

import com.example.rwado.financialcalc.Model.MyPlaces;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApiService {
    @GET
    Call<MyPlaces> getNearbyPlaces(@Url String url);
}
