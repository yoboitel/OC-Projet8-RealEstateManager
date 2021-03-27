package com.openclassrooms.realestatemanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils.NotificationHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

public class NewEstateActivity extends AppCompatActivity {

    private AutoCompleteTextView textInputLayoutType, textInputLayoutStatus;
    private FloatingActionButton fabInsertEstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_estate);
        setTitle("New Estate");
        //Display back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Handle estate type dropdown menu
        String[] typeArray = new String[] {"Apartment", "Loft", "House"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_material, typeArray);
        textInputLayoutType = findViewById(R.id.spinnerEstateCategory);
        textInputLayoutType.setAdapter(adapter);

        //Handle estate type dropdown menu
        String[] statusArray = new String[] {"Available", "Sold"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, R.layout.dropdown_material, statusArray);
        textInputLayoutStatus = findViewById(R.id.spinnerEstateStatus);
        textInputLayoutStatus.setAdapter(adapterStatus);

        //Handle Fab to add new estate to db
        fabInsertEstate = findViewById(R.id.fabCreateNewEstate);
        fabInsertEstate.setOnClickListener(view -> {

            //Call the asyncTask to insert estate in room database
            InsertEstate insertEstate = new InsertEstate();
            insertEstate.execute();

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class InsertEstate extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Estate estateToAdd = new Estate("Apartment", "130000");
            DbHelper.getInstance(getApplicationContext()).getAppDatabase().estateDao().insert(estateToAdd);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(getApplicationContext(), "Estate successfully added", Toast.LENGTH_LONG).show();

            //TODO: Show success notification
            NotificationHelper.sendNotifications(getBaseContext(), "Informations", "New estate successfully added");
        }
    }

}
