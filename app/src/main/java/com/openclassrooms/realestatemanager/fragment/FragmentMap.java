package com.openclassrooms.realestatemanager.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.geocoder.service.models.GeocoderResponse;
import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FragmentMap extends Fragment implements GoogleMap.OnMarkerClickListener {

    private final int ZOOM_LEVEL = 15; //This goes up to 21
    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;

    public FragmentMap() {
        // Required empty public constructor
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            gMap = googleMap;

            //Method to ask for location permission and move map to user's location
            getDeviceLocation();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            //Check if user is connected to internet
            if (Utils.isInternetAvailable(requireActivity()))
                mapFragment.getMapAsync(callback);
            else
                Toast.makeText(requireActivity(), "You must be connected to internet", Toast.LENGTH_SHORT).show();
        }
    }

    //Go to estate's detail fragment after a click on its marker
    @Override
    public boolean onMarkerClick(Marker marker) {

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, new FragmentDetail(Integer.valueOf(Objects.requireNonNull(marker.getTag()).toString())))
                .addToBackStack(null)
                .commit();

        return false;
    }

    //Async task to retrieve and display estates on map
    class getAllEstates extends AsyncTask<Void, Void, List<Estate>> {

        @Override
        protected List<Estate> doInBackground(Void... voids) {

            List<Estate> estateList = DbHelper.getInstance(requireActivity()).getAppDatabase().estateDao().getEstates();
            return estateList;
        }

        @Override
        protected void onPostExecute(List<Estate> aVoid) {
            super.onPostExecute(aVoid);

            for (Estate estate : aVoid){
                //Find coordinates from address
                //Use geocoding to get coordinates of the estate's address string and generate the static map preview with the coordinates.
                MapboxGeocoder mapboxGeocoder = new MapboxGeocoder.Builder()
                        .setAccessToken(getActivity().getResources().getString(R.string.mapbox_public_access_token))
                        .setLocation(estate.getAddress())
                        .build();
                mapboxGeocoder.enqueue(new Callback<GeocoderResponse>() {
                    @Override
                    public void onResponse(Response<GeocoderResponse> response, Retrofit retrofit) {

                        if (response.body().getFeatures().size() > 0) {

                            //TODO : Add google maps markers to the google map
                            //Put the marker on the map
                            gMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(response.body().getFeatures().get(0).getLatitude(), response.body().getFeatures().get(0).getLongitude()))
                                    .title(estate.getAddress()))
                                    .setTag(estate.getId());

                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(requireActivity(), "Mapbox geocoding fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    //Find nearby locations
    public void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permission not granted so ask for it
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //Permission is granted so retrieve the user's last position
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {

                    //Setup Google Map
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), ZOOM_LEVEL));
                    gMap.setMyLocationEnabled(true);
                    gMap.setOnMarkerClickListener(this);
                    gMap.getUiSettings().setMyLocationButtonEnabled(true);

                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //TODO: Call async task to display markers
                        new getAllEstates().execute();
                    } else {
                        getDeviceLocation();
                    }


                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
                getDeviceLocation();
            }
        }
    }

    //Method to edit marker size
    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", requireContext().getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }


}
