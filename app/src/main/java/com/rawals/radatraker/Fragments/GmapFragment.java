package com.rawals.radatraker.Fragments;

/**
 * Created by Ramon on 12/3/16.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.rawals.radatraker.R;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rawals.radatraker.*;
import java.util.ArrayList;
import java.util.List;


public class GmapFragment extends ImportFragment.AbstractMapActivity implements
        OnMapReadyCallback, OnInfoWindowClickListener,
        OnMyLocationChangeListener, View.OnClickListener {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmaps, container,false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

private boolean needsInit=false;
        Button bmapa;
        Button bhibrido;
        Button bterreno;
        Button bIniciar;
        Button bParar;
private Polyline polilinea;
private List<LatLng> list = new ArrayList<>();
        LocationManager locationManager;
        ClickLongListener clickLongListener;
        AlertDialog alert = null;
        Location lastKnownLocation;

//private GoogleMap mMap;
private GoogleMap map;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (readyToGo()) {
        setContentView(R.layout.activity_main);

        MapFragment mapFrag=
        (MapFragment)getFragmentManager().findFragmentById(R.id.map);

        if (savedInstanceState == null) {
        needsInit=true;
        }

        mapFrag.getMapAsync(this);

        }
        bmapa = (Button)findViewById(R.id.bmapa);
        bhibrido = (Button)findViewById(R.id.bhibrido);
        bterreno = (Button)findViewById(R.id.bterreno);
        bIniciar = (Button)findViewById(R.id.bINiciar);
        bParar = (Button)findViewById(R.id.bParar);


        bmapa.setOnClickListener(this);
        bhibrido.setOnClickListener(this);
        bterreno.setOnClickListener(this);
        bIniciar.setOnClickListener(this);
        bParar.setOnClickListener(this);


        }

@Override
public void onClick(View v) {

        switch (v.getId()){

        case R.id.bmapa:
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        break;
        case R.id.bhibrido:
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        break;

        case R.id.bterreno:
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        break;

        case R.id.bINiciar:

        onMyLocationChange(lastKnownLocation);

        break;

        case R.id.bParar:



        break;


default:
        break;

        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //Miramos si esta encendido en GPS
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
        //Alerta para que el usuario active el GPS
        AlertNoGps();
        }

        }

@Override
public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        //map.setMyLocationEnabled(true);

        if (needsInit) {
        //Centramos la imagen del mapa al arrancar
        CameraUpdate center=
        CameraUpdateFactory.newLatLng(new LatLng(40.416646,
        -3.703818));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(4);

        map.moveCamera(center);
        map.animateCamera(zoom);


        }


        //map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);


        //Recoge las coordenadas cada 5 segundos
        map.setOnMyLocationChangeListener(this);

        //Agregamos un ClickLongListener para manejar cuando el usuario haga long click sobre el mapa
        addClickLongListener();
        }

@Override
public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
        }

@Override
public void onMyLocationChange(Location lastKnownLocation) {


        LatLng latLong = new LatLng(lastKnownLocation.getLatitude(),
        lastKnownLocation.getLongitude());
        //Recogemos las coordenadas en un arrayList
        list.add(latLong);

        ruta();
        }

private void addMarker(GoogleMap map, LatLng position, String titulo, String info) {


        // Agregamos marcadores para indicar sitios de interéses.
        Marker myMaker = map.addMarker(new MarkerOptions()
        .position(position)//Agrega la posicion
        .title(titulo)  //Agrega un titulo al marcador
        .snippet(info)); //Color del marcador



        }

public void addClickLongListener(){
        map.setOnMapLongClickListener(new ClickLongListener(map));


        }


private void AlertNoGps() {

final AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setMessage("El GPS esta desactivado, ¿Quieres activarlo?")
        .setCancelable(false)
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {

public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
       // map.setMyLocationEnabled(true);
        }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
        dialog.cancel();
        }
        });

        alert = buider.create();
        alert.show();
        }


//Liberar memoria despues de la alerta
@Override
protected void onDestroy(){
        super.onDestroy();
        if (alert != null){
        alert.dismiss();
        }
        }

public void ruta () {

        PolylineOptions po;

        if (polilinea == null) {

        po = new PolylineOptions();

        for (int i = 0, tam = list.size(); i < tam; i++) {
        po.add(list.get(i));

        }
        po.color(Color.BLACK);
        polilinea = map.addPolyline(po);
        } else {
        polilinea.setPoints(list);
        }

        }

}
