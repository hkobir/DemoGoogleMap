package com.hk.demogooglemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FloatingActionButton myLocation;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocationPermission();

        myLocation = findViewById(R.id.fab);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get current location event
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();

            }
        });
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissions, 0);
            } else {

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng comilla = new LatLng(23.4615864,91.1788489);
        googleMap.addMarker(new MarkerOptions().position(comilla)).setTitle("Comilla,Home Town");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(comilla, 17));

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void getCurrentLocation() {

        FusedLocationProviderClient locationProviderClient = new FusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        Task result = locationProviderClient.getLastLocation();
        result.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()) {
                    Location cLocation = (Location) task.getResult();
                    LatLng cLatlong = new LatLng(cLocation.getLatitude(), cLocation.getLongitude());
                    String address = getAddress(cLocation.getLatitude(), cLocation.getLongitude());
                    map.addMarker(new MarkerOptions().position(cLatlong).title(address));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(cLatlong, 17));

                }
            }
        });
    }

    private String getAddress(double latitude, double longitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {

                address = addresses.get(0).getAddressLine(0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
}
