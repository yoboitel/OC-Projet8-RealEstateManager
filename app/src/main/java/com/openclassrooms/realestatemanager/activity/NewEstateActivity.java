package com.openclassrooms.realestatemanager.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.NotificationHelper;
import com.openclassrooms.realestatemanager.adapter.PhotoAdapter;
import com.opensooq.supernova.gligar.GligarPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewEstateActivity extends AppCompatActivity {

    private AutoCompleteTextView fieldType, fieldStatus;
    private FloatingActionButton fabInsertEstate;
    private TextView tvEstateAddPhotos;
    private TextInputEditText fieldDateAvailable, fieldDateSold;

    private TextInputLayout fieldPrice, fieldSurface, fieldRooms, fieldDescription, fieldAddress, fieldAgent;
    private Chip chipSchools, chipShops, chipParks, chipHospitals;
    private ChipGroup chipGroup;

    private RecyclerView rcPhoto;
    private ArrayList<String> photoUrls = new ArrayList<>();
    private ArrayList<String> photoDescriptions = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private Long availableLong = null;
    private Long soldLong = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estate);
        //Initialization
        initialization();

        //Handle estate type dropdown menu
        String[] typeArray = new String[]{"Apartment", "Loft", "House"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_material, typeArray);
        fieldType.setAdapter(adapter);

        //Handle estate status dropdown menu
        String[] statusArray = new String[]{"Available", "Sold"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, R.layout.dropdown_material, statusArray);
        fieldStatus.setAdapter(adapterStatus);

        //Handle Fab to add new estate to db
        fabInsertEstate.setOnClickListener(view -> {
            //Check every fields are filled before inserting the estate in room database.
            checkEverythingIsCorrectBeforeSaving();

        });

        //Handle photos selection
        tvEstateAddPhotos.setOnClickListener(view -> {
            //Open photo picker library, result in onActivityResult
            new GligarPicker().requestCode(42).withActivity(NewEstateActivity.this).limit(1).show();
        });

        //Handle date selection
        fieldDateAvailable.setOnClickListener(view -> {
            MaterialDatePicker mp = MaterialDatePicker.Builder.datePicker().build();
            mp.show(getSupportFragmentManager(), "picker");
            mp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    availableLong = Long.valueOf(selection.toString());
                    fieldDateAvailable.setText(getFormattedDateFromLong(Long.parseLong(selection.toString())));
                }
            });
        });
        fieldDateSold.setOnClickListener(view -> {
            MaterialDatePicker mp = MaterialDatePicker.Builder.datePicker().build();
            mp.show(getSupportFragmentManager(), "picker");
            mp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    soldLong = Long.valueOf(selection.toString());
                    fieldDateSold.setText(getFormattedDateFromLong(Long.parseLong(selection.toString())));
                }
            });
        });

    }

    private void initialization() {
        setTitle("New Estate");
        //Display back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fieldType = findViewById(R.id.spinnerEstateCategory);
        fieldPrice = findViewById(R.id.textInputLayoutEstatePrice);
        fieldSurface = findViewById(R.id.textInputLayoutEstateSurface);
        fieldRooms = findViewById(R.id.textInputLayoutEstateRooms);
        fieldDescription = findViewById(R.id.textInputLayoutEstateDescription);
        fieldAddress = findViewById(R.id.textInputLayoutEstateAddressStreet);
        chipGroup = findViewById(R.id.chipsGroup);
        chipSchools = findViewById(R.id.chipSchools);
        chipShops = findViewById(R.id.chipShops);
        chipParks = findViewById(R.id.chipParks);
        chipHospitals = findViewById(R.id.chipHospitals);
        fieldStatus = findViewById(R.id.spinnerEstateStatus);
        fieldAgent = findViewById(R.id.textInputLayoutEstateAgent);
        fieldDateAvailable = findViewById(R.id.textInputLayoutEstateDateAvailable);
        fieldDateSold = findViewById(R.id.textInputLayoutEstateDateSold);
        tvEstateAddPhotos = findViewById(R.id.tvEstatePhotos);
        fabInsertEstate = findViewById(R.id.fabCreateNewEstate);
        rcPhoto = findViewById(R.id.rcPhotoList);
        //Setup photos recyclerview
        rcPhoto.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        rcPhoto.setHasFixedSize(true);
    }

    //Go back to home when back arrow is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Async task to insert new estate in Room database
    class InsertEstate extends AsyncTask<Void, Void, Void> {

        private Estate newEstate;

        InsertEstate(Estate estate) {
            this.newEstate = estate;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DbHelper.getInstance(getApplicationContext()).getAppDatabase().estateDao().insert(newEstate);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Show success notification
            NotificationHelper.sendNotifications(getBaseContext(), "Informations", "New estate successfully added");
            //Bring back to home
            finish();
        }
    }

    //Get Photo pick result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 42) {
            String pathsList[] = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // return list of selected images paths.

            //show title popup
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(getBaseContext());
            alert.setTitle("Photo description");
            alert.setView(edittext);
            alert.setPositiveButton("Add", (dialog, whichButton) -> {
                //Must add title to image
                if (!(TextUtils.isEmpty(edittext.getText().toString()))) {
                    NewEstateActivity.this.addPhoto(pathsList[0], edittext.getText().toString());
                } else
                    Toast.makeText(NewEstateActivity.this, "Photo description required", Toast.LENGTH_SHORT).show();
            });
            alert.show();
        }
    }

    private void addPhoto(String stringUrl, String title) {
        //Add data to both lists
        photoUrls.add(stringUrl);
        photoDescriptions.add(title);
        //Add both lists in the adapter
        photoAdapter = new PhotoAdapter(getBaseContext(), photoUrls, photoDescriptions, true);
        //Set the adapter and notify
        rcPhoto.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
    }

    private String getFormattedDateFromLong(Long dateLong) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(dateLong));
    }

    private void checkEverythingIsCorrectBeforeSaving() {

        if (TextUtils.isEmpty(fieldType.getText()))
            showSnack("Please specify a type");
        else if (TextUtils.isEmpty(fieldPrice.getEditText().getText().toString()))
            showSnack("Please specify a price");
        else if (TextUtils.isEmpty(fieldSurface.getEditText().getText().toString()))
            showSnack("Please specify a surface");
        else if (TextUtils.isEmpty(fieldRooms.getEditText().getText().toString()))
            showSnack("Please specify rooms count");
        else if (TextUtils.isEmpty(fieldDescription.getEditText().getText().toString()))
            showSnack("Please specify description");
        else if (TextUtils.isEmpty(fieldAddress.getEditText().getText().toString()))
            showSnack("Please specify address");
        else if (TextUtils.isEmpty(fieldStatus.getText().toString()))
            showSnack("Please specify status");
        else if (fieldStatus.getText().toString().equals("Available") && (!(fieldDateSold.getText().toString().isEmpty())))
            fieldDateSold.getText().clear();
        else if (fieldStatus.getText().toString().equals("Sold") && (fieldDateSold.getText().toString().isEmpty()))
            showSnack("Please specify the sold date or change status");
        else if (TextUtils.isEmpty(fieldAgent.getEditText().getText().toString()))
            showSnack("Please specify an agent");
        else if (TextUtils.isEmpty(fieldDateAvailable.getText().toString()))
            showSnack("Please specify the estate's available date");
        else if (photoUrls.isEmpty())
            showSnack("Please add at least one photo");
        else {
            //Call the asyncTask to insert estate in room database

            Estate estate = new Estate();
            estate.setType(fieldType.getText().toString());
            estate.setPrice(Integer.valueOf(fieldPrice.getEditText().getText().toString()));
            estate.setSurface(Integer.valueOf(fieldSurface.getEditText().getText().toString()));
            estate.setRooms(Integer.valueOf(fieldRooms.getEditText().getText().toString()));
            estate.setDescription(fieldDescription.getEditText().getText().toString());
            estate.setAddress(fieldAddress.getEditText().getText().toString());
            estate.setSchools(chipStateIndicator(chipSchools));
            estate.setShops(chipStateIndicator(chipShops));
            estate.setParks(chipStateIndicator(chipParks));
            estate.setHospitals(chipStateIndicator(chipHospitals));
            estate.setStatus(fieldStatus.getText().toString());
            estate.setAgent(fieldAgent.getEditText().getText().toString());
            estate.setDateAvailable(new Date(availableLong));
            if (soldLong != null)
                estate.setDateSold(new Date(soldLong));
            else
                estate.setDateSold(null);
            estate.setPhotoUrls(photoUrls);
            estate.setPhotoDescriptions(photoDescriptions);

            InsertEstate insertEstate = new InsertEstate(estate);
            insertEstate.execute();
        }

    }

    private void showSnack(String text) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show();
    }

    private boolean chipStateIndicator(Chip actualChip) {
        boolean state = false;
        for (Integer chip : chipGroup.getCheckedChipIds()) {
            if (chip.equals(actualChip.getId())) {
                state = true;
            }
        }
        return state;
    }
}