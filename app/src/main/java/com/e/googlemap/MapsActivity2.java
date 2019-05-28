package com.e.googlemap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import model.LatitudeLongitude;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudeList;
    Marker markerName;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);
        
        fillArrayListAndSetAdapter();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCity.getText().toString())) {
                    etCity.setError("Please enter a place name");
                    return;
                }
            int position = SearchArrayList(etCity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(MapsActivity2.this, "Location not found by name :" +etCity.getText().toString() , Toast.LENGTH_SHORT).show();
                
            }

            private void loadMap(int position) {
                if (markerName!=null)
                {
                    markerName.remove();
                }
                double latitude = latitudeLongitudeList.get(position).getLat();
                double longitude = latitudeLongitudeList.get(position).getLon();
                String marker = latitudeLongitudeList.get(position).getMarker();
                center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
                zoom = CameraUpdateFactory.zoomTo(17);
                markerName = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(marker));
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
                mMap.getUiSettings().setZoomControlsEnabled(true);

            }

            private int SearchArrayList(String toString) {
                for (int i =0; i < latitudeLongitudeList.size(); i++) {
                    if (latitudeLongitudeList.get(i).getMarker().contains(toString)) {
                        return i;
                    }
                }
                return -1;
            }

        });
    }

    private void fillArrayListAndSetAdapter() {
        latitudeLongitudeList = new ArrayList<>();
        latitudeLongitudeList.add(new LatitudeLongitude(28.127721, 82.303997, "Acharya Niwas"));
        latitudeLongitudeList.add(new LatitudeLongitude(28.127704, 82.303615, "TT Training Center"));
        latitudeLongitudeList.add(new LatitudeLongitude(28.127451, 82.304068, "Tulsipur Multipurpose Cupboard Hall"));

        String[] data = new String[latitudeLongitudeList.size()];
        for (int i=0; i< data.length; i++) {
            data[i] = latitudeLongitudeList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                MapsActivity2.this, android.R.layout.simple_list_item_1, data
        );
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(28.127721, 82.303997));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }
}
