package com.example.rwado.financialcalc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rwado.financialcalc.Model.MyPlaces;
import com.example.rwado.financialcalc.Model.Results;
import com.example.rwado.financialcalc.Remote.IGoogleApiService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Atms extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private static final int MY_PERMISSION_CODE = 1000;
    private GoogleMap mMap;
    private IGoogleApiService mService;
    private FusedLocationProviderClient mFusedLocationClient;
    private Double latitude, longitude;
    private int radius = 1000;
    private double area = radius;
    private Button buttonShowAtms;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.atms_layout, container, false);

        checkPermission();

        initializeMap();

        initializeService();

        initializeViews(view);

        setViewsListeners();

        return view;
    }

    private void setViewsListeners() {
        buttonShowAtms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearbyPlace("atm");
            }
        });
    }

    private void initializeViews(View view) {
        buttonShowAtms = (Button) view.findViewById(R.id.buttonShowAtms);
    }

    private void initializeService() {
        mService = Common.getGoogleApiService();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void nearbyPlace(final String type) {
        mMap.clear();
        String url = getUrl(latitude, longitude, type, radius);
        mService.getNearbyPlaces(url).enqueue(new Callback<MyPlaces>() {
            @Override
            public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                if (response.isSuccessful()) {
                    int numberOfResults = response.body().getResults().length;
                    if (numberOfResults < 15) {
                        increaseSearchArea(type);
                        return;
                    }
                    for (int i = 0; i < numberOfResults; i++) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        Results googlePlace = response.body().getResults()[i];
                        double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                        double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                        String placeName = googlePlace.getName();
                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName);

                        if (type.equals("atm")) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        }
                        mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                    }
                }
            }

            @Override
            public void onFailure(Call<MyPlaces> call, Throwable t) {

            }
        });
    }

    private void increaseSearchArea(String type) {
        area = Math.PI * Math.pow(radius, 2);
        double radiusDiff = Math.sqrt((area + 5000000) / Math.PI) - radius;
        radius += radiusDiff;
        nearbyPlace(type);
    }

    private String getUrl(Double latitude, Double longitude, String placeType, int radius) {
        StringBuilder googlePlacesUrl = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + String.valueOf(radius));
        googlePlacesUrl.append("&language=" + getResources().getString(R.string.language_pl_en));
        googlePlacesUrl.append("&type=" + placeType);
        googlePlacesUrl.append("&key=" + getResources().getString(R.string.browser_key));
        Log.d("getUrlTag", googlePlacesUrl.toString());
        return googlePlacesUrl.toString();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(20.0f);

        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location == null) {
                    requestNewLocationData();
                } else {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
                }

            }
        });
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_CODE);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.nearby_atms));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
