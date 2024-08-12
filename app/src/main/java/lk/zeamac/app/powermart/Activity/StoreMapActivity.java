package lk.zeamac.app.powermart.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import lk.zeamac.app.powermart.R;

public class StoreMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);


        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.mapId);
        mapFragment.getMapAsync(StoreMapActivity.this);



        findViewById(R.id.imageViewback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreMapActivity.this, OrderInfoActivity.class );
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreMapActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
               map = googleMap;

        LatLng latLng = new LatLng(7.334976751879802, 80.29997670073774);

        map.addMarker(new MarkerOptions().position(latLng).title("Power Mart Shop"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
    }
}