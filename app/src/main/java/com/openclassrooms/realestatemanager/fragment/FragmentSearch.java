package com.openclassrooms.realestatemanager.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapter.EstateAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FragmentSearch extends Fragment {

    private AutoCompleteTextView fieldType, fieldStatus;
    private FloatingActionButton fabSearch;
    private TextInputLayout fieldPriceMin, fieldPriceMax, fieldSurfaceMin, fieldSurfaceMax, fieldRoomsMin, fieldRoomsMax;
    private TextInputEditText fieldDateMin, fieldDateMax;
    private RecyclerView rcSearchResult;
    private Date minDate, maxDate;

    public FragmentSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        initialization(v);

        //Handle date search selection
        fieldDateMin.setOnClickListener(view -> {
            MaterialDatePicker mp = MaterialDatePicker.Builder.datePicker().build();
            mp.show(requireActivity().getSupportFragmentManager(), "picker");
            mp.addOnPositiveButtonClickListener(selection -> {
                minDate = new Date(Long.parseLong(selection.toString()));
                fieldDateMin.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.parseLong(selection.toString()))));
            });
        });
        fieldDateMax.setOnClickListener(view -> {
            MaterialDatePicker mp = MaterialDatePicker.Builder.datePicker().build();
            mp.show(requireActivity().getSupportFragmentManager(), "picker");
            mp.addOnPositiveButtonClickListener(selection -> {
                maxDate = new Date(Long.parseLong(selection.toString()));
                fieldDateMax.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.parseLong(selection.toString()))));
            });
        });

        //Call the search async task when click the fab
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if fields are empty before calling the request
                if (TextUtils.isEmpty(fieldSurfaceMin.getEditText().getText().toString()) ||
                        TextUtils.isEmpty(fieldSurfaceMax.getEditText().getText().toString()) ||
                        TextUtils.isEmpty(fieldPriceMin.getEditText().getText().toString()) ||
                        TextUtils.isEmpty(fieldPriceMax.getEditText().getText().toString()) ||
                        TextUtils.isEmpty(fieldRoomsMin.getEditText().getText().toString()) ||
                        TextUtils.isEmpty(fieldRoomsMax.getEditText().getText().toString())) {
                    Toast.makeText(requireActivity(), "fields can't be empty", Toast.LENGTH_SHORT).show();
                } else {

                    new searchEstates(fieldType.getText().toString(),
                            Integer.valueOf(fieldSurfaceMin.getEditText().getText().toString()),
                            Integer.valueOf(fieldSurfaceMax.getEditText().getText().toString()),
                            Integer.valueOf(fieldPriceMin.getEditText().getText().toString()),
                            Integer.valueOf(fieldPriceMax.getEditText().getText().toString()),
                            Integer.valueOf(fieldRoomsMin.getEditText().getText().toString()),
                            Integer.valueOf(fieldRoomsMax.getEditText().getText().toString()),
                            fieldStatus.getText().toString(),
                            minDate,
                            maxDate
                    ).execute();
                }
            }
        });

        return v;
    }
    
    private void initialization(View v) {
        fieldType = v.findViewById(R.id.searchSpinnerEstateCategory);
        fieldStatus = v.findViewById(R.id.searchSpinnerEstateStatus);
        setDropdownMenusForTypeAndStatus();
        fieldSurfaceMin = v.findViewById(R.id.searchMinSurface);
        fieldSurfaceMax = v.findViewById(R.id.searchMaxSurface);
        fieldPriceMin = v.findViewById(R.id.searchMinPrice);
        fieldPriceMax = v.findViewById(R.id.searchMaxPrice);
        fieldRoomsMin = v.findViewById(R.id.searchMinRooms);
        fieldRoomsMax = v.findViewById(R.id.searchMaxRooms);
        fieldDateMin = v.findViewById(R.id.searchMinDate);
        fieldDateMax = v.findViewById(R.id.searchMaxDate);
        fabSearch = v.findViewById(R.id.fabSearchEstate);
        rcSearchResult = v.findViewById(R.id.rcSearchResult);
        rcSearchResult.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rcSearchResult.setHasFixedSize(true);
    }

    private void setDropdownMenusForTypeAndStatus(){
        //Handle estate type dropdown menu
        String[] typeArray = new String[]{"Apartment", "Loft", "House"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_material, typeArray);
        fieldType.setAdapter(adapter);

        //Handle estate status dropdown menu
        String[] statusArray = new String[]{"Available", "Sold"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_material, statusArray);
        fieldStatus.setAdapter(adapterStatus);
    }

    //AsyncTask
    class searchEstates extends AsyncTask<Void, Void, List<Estate>> {

        private String type;
        private Integer minSurface;
        private Integer maxSurface;
        private Integer minPrice;
        private Integer maxPrice;
        private Integer minRooms;
        private Integer maxRooms;
        private String status;
        private Date dateMin;
        private Date dateMax;

        searchEstates(String type, Integer minSurface, Integer maxSurface, Integer minPrice, Integer maxPrice, Integer minRooms, Integer maxRooms, String status, Date dateMin, Date dateMax) {
            this.type = type;
            this.minSurface = minSurface;
            this.maxSurface = maxSurface;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.minRooms = minRooms;
            this.maxRooms = maxRooms;
            this.status = status;
            this.dateMin = dateMin;
            this.dateMax = dateMax;
        }

        @Override
        protected List<Estate> doInBackground(Void... voids) {

            List<Estate> estateList = DbHelper.getInstance(requireActivity()).getAppDatabase().estateDao().searchEstates(type, minSurface, maxSurface, minPrice, maxPrice, minRooms, maxRooms, status, dateMin, dateMax);
            return estateList;
        }

        @Override
        protected void onPostExecute(List<Estate> aVoid) {
            super.onPostExecute(aVoid);

            if (aVoid.isEmpty())
                Toast.makeText(requireContext(), "No estates found", Toast.LENGTH_SHORT).show();
            else {
                EstateAdapter estateAdapter = new EstateAdapter(requireActivity(), aVoid, new EstateAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, new FragmentDetail(aVoid.get(position).getId()))
                                .addToBackStack(null)
                                .commit();
                    }
                });
                rcSearchResult.setAdapter(estateAdapter);
                estateAdapter.notifyDataSetChanged();
            }
        }
    }

}
