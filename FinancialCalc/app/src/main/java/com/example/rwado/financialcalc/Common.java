package com.example.rwado.financialcalc;

import com.example.rwado.financialcalc.Remote.IGoogleApiService;
import com.example.rwado.financialcalc.Remote.RetrofitClient;

public class Common {
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleApiService getGoogleApiService() {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleApiService.class);
    }
}

