package com.openclassrooms.realestatemanager.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.api.staticmap.v1.models.StaticMarkerAnnotation;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.geocoder.service.models.GeocoderResponse;
import com.mapbox.geojson.Point;
import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.activity.NewEstateActivity;
import com.openclassrooms.realestatemanager.adapter.PhotoAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FragmentDetail extends Fragment {

    private Integer id;
    private RecyclerView rcPhotos;
    private TextView tvDetailDesc, tvDetailType, tvDetailRoom, tvDetailPrice, tvDetailSurface, tvDetailAddress, tvDetailAgent, tvDetailAvailableDate, tvDetailSoldDate;
    private ImageView ivDetailAddressPreview;
    private Chip chipSchool, chipSchops, chipParks, chipHospitals;
    private FloatingActionButton fabEditEstate;

    public FragmentDetail() {
        // Required empty public constructor
    }

    public FragmentDetail(Integer id) {
        // Constructor to pass the clicked estate id
        this.id = id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        //Init
        initialization(v);

        //Fab edit estate click
        fabEditEstate.setOnClickListener(view -> {
            Intent i = new Intent(requireActivity(), NewEstateActivity.class);
            i.putExtra("id", id);
            startActivity(i);
        });

        //Call AsyncTask to get estate detail
        if (id != null) {
            new getEstateById(id).execute();
        }

        return v;
    }

    class getEstateById extends AsyncTask<Void, Void, Estate> {

        private int estateId;

        getEstateById(int estateId) {
            this.estateId = estateId;
        }

        @Override
        protected Estate doInBackground(Void... voids) {
            Estate estate = DbHelper.getInstance(requireActivity()).getAppDatabase().estateDao().getEstateById(estateId);
            return estate;
        }
        @Override
        protected void onPostExecute(Estate aVoid) {
            super.onPostExecute(aVoid);
            fillDetailViewsWithRoomData(aVoid);
        }
    }

    private void fillDetailViewsWithRoomData(Estate estate){

        //Set photos, set the adapter and notify
        rcPhotos.setAdapter(new PhotoAdapter(requireActivity(), estate.getPhotoUrls(), estate.getPhotoDescriptions(), false));
        //Set estate description
        tvDetailDesc.setText(estate.getDescription());
        //Set estate type
        tvDetailType.setText(estate.getType());
        //Set estate room count
        tvDetailRoom.setText(String.valueOf(estate.getRooms()));
        //Set estate price
        tvDetailPrice.setText(String.format("$%s", estate.getPrice()));
        //Set estate surface
        tvDetailSurface.setText(String.format(Locale.getDefault(), "%dm2", estate.getSurface()));
        //Set estate agent
        tvDetailAgent.setText(estate.getAgent());
        //Set estate available date
        tvDetailAvailableDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(estate.getDateAvailable()));
        //Set estate sold date
        if (estate.getDateSold() != null)
            tvDetailSoldDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(estate.getDateSold()));
        else
            tvDetailSoldDate.setText("Not sold yet");
        //Set estate nearby interests
        setNearbyInterestChips(estate);
        //Set estate address
        tvDetailAddress.setText(estate.getAddress());
        //Set estate address map preview
        displayStaticMapFromGeocodedAddress(estate.getAddress());
    }

    private void initialization(View v){
        rcPhotos = v.findViewById(R.id.rcDetailPhotos);
        rcPhotos.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcPhotos.setHasFixedSize(true);
        tvDetailDesc = v.findViewById(R.id.tvDetailDesc);
        tvDetailType = v.findViewById(R.id.tvDetailType);
        tvDetailRoom = v.findViewById(R.id.tvDetailRooms);
        tvDetailPrice = v.findViewById(R.id.tvDetailPrice);
        tvDetailSurface = v.findViewById(R.id.tvDetailSurface);
        tvDetailAddress = v.findViewById(R.id.tvDetailAddress);
        tvDetailAddress.setSelected(true); //Allow marquee to automatically scroll
        ivDetailAddressPreview = v.findViewById(R.id.ivDetailAddressPreview);
        tvDetailAgent = v.findViewById(R.id.tvDetailAgent);
        tvDetailAvailableDate = v.findViewById(R.id.tvDetailAvailableDate);
        tvDetailSoldDate = v.findViewById(R.id.tvDetailSoldDate);
        chipSchool = v.findViewById(R.id.chipDetailSchools);
        chipSchops = v.findViewById(R.id.chipDetailShops);
        chipParks = v.findViewById(R.id.chipDetailParks);
        chipHospitals = v.findViewById(R.id.chipDetailHospitals);
        fabEditEstate = v.findViewById(R.id.fabEditEstate);
    }

    private void displayStaticMapFromGeocodedAddress(String address) {

        //Use geocoding to get coordinates of the estate's address string and generate the static map preview with the coordinates.
        MapboxGeocoder mapboxGeocoder = new MapboxGeocoder.Builder()
                .setAccessToken(getActivity().getResources().getString(R.string.mapbox_public_access_token))
                .setLocation(address)
                .build();
        mapboxGeocoder.enqueue(new Callback<GeocoderResponse>() {
            @Override
            public void onResponse(Response<GeocoderResponse> response, Retrofit retrofit) {

                if (response.body().getFeatures().size() > 0) {

                    //Create the static market at the estate's address
                    List<StaticMarkerAnnotation> staticMarkerAnnotations = new ArrayList<>();
                    staticMarkerAnnotations.add(StaticMarkerAnnotation.builder()
                            .lnglat(Point.fromLngLat(response.body().getFeatures().get(0).getLongitude(), response.body().getFeatures().get(0).getLatitude()))
                            .color(255, 0, 0)
                            .build());

                    //Create static map preview from decoded coordinates from geocoder
                    MapboxStaticMap staticImage = MapboxStaticMap.builder()
                            .accessToken(getActivity().getResources().getString(R.string.mapbox_public_access_token))
                            .styleId(StaticMapCriteria.STREET_STYLE)
                            .cameraPoint(Point.fromLngLat(response.body().getFeatures().get(0).getLongitude(), response.body().getFeatures().get(0).getLatitude())) // Image's centerpoint on map
                            .cameraZoom(14)
                            .staticMarkerAnnotations(staticMarkerAnnotations)
                            .width(320) // Image width
                            .height(320) // Image height
                            .retina(true) // Retina 2x image will be returned
                            .build();
                    //Set generated image with picasso
                    String generatedImageUrl = staticImage.url().toString();
                    Picasso.get().load(generatedImageUrl).into(ivDetailAddressPreview);
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(requireActivity(), "Mapbox geocoding fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNearbyInterestChips(Estate estate){
        chipSchool.setChecked(estate.getSchools());
        chipSchool.setCheckable(false);
        chipSchops.setChecked(estate.getShops());
        chipSchops.setCheckable(false);
        chipParks.setChecked(estate.getParks());
        chipParks.setCheckable(false);
        chipHospitals.setChecked(estate.getHospitals());
        chipHospitals.setCheckable(false);
    }

    //Refresh fragment data after edited from activity
    @Override
    public void onResume() {
        super.onResume();
        //Call AsyncTask to get estate detail
        if (id != null) {
            new getEstateById(id).execute();
        }
    }
}
