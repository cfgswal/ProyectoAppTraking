package com.rawals.radatraker.Fragments;

/**
 * Created by Ramon on 13/3/16.
 */
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

public class ClickLongListener implements OnMapLongClickListener {
    private static final String CLASS_TAG = ClickLongListener.class.getName();
    private GoogleMap map;
    private Polyline polilinea;
    private List<LatLng> list = new ArrayList<>();

    public ClickLongListener(GoogleMap mMap) {
        this.map = mMap;

    }

    @Override
    public void onMapLongClick(LatLng latLong) {
        //Recoge las coordenas del marcador


        String cad=String.valueOf((latLong.latitude));
        cad = cad.substring(0,9);

        String cad2=String.valueOf((latLong.longitude));
        cad2 = cad2.substring(0,8);

        Log.v(CLASS_TAG, "latitude: " + String.valueOf(latLong.latitude));
        Log.v(CLASS_TAG, "longitude: " + String.valueOf(latLong.longitude));

        this.map.addMarker(new MarkerOptions().position(latLong)
                .title("Coordenadas:")
                .snippet("Latitud: " + String.valueOf(cad)
                        + "\nLongitud: " + String.valueOf(cad2)));


    }


    }