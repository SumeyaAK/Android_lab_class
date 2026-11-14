package com.example.android_lab_class;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.android_lab_class.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnPoiClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private TextView text;
    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        text = binding.textv;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkLocationPermissionAndRequest();
    }

    private void checkLocationPermissionAndRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (locationManager != null && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng jimma = new LatLng(7.6736450324877294, 36.83693743308046);
        mMap.addMarker(new MarkerOptions().position(jimma).title("Marker in jimma"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jimma, 15f));

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setIndoorLevelPickerEnabled(true);

        mMap.setOnPoiClickListener(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        text.setText("lat: " + location.getLatitude() + " long: " + location.getLongitude());
    }

    public void searchLocation(View view) {
        String locationStr = "jimma";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationStr, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(locationStr));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                Toast.makeText(this, "Lat: " + address.getLatitude() + " Long: " + address.getLongitude(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPoiClick(@NonNull PointOfInterest poi) {
        Toast.makeText(this, "Clicked: " + poi.name + "\nPlace ID: " + poi.placeId + "\nLatitude: " + poi.latLng.latitude + " Longitude: " + poi.latLng.longitude, Toast.LENGTH_LONG).show();
        text.setText("Latitude: " + poi.latLng.latitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(@NonNull String provider) {}

    @Override
    public void onProviderDisabled(@NonNull String provider) {}
}
