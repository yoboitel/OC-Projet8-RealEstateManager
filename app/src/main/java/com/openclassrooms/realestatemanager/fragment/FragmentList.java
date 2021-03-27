package com.openclassrooms.realestatemanager.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapter.EstateAdapter;

import java.util.List;

public class FragmentList extends Fragment {

    RecyclerView rcListEstate;

    public FragmentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        //Setup recyclerview
        rcListEstate = v.findViewById(R.id.rcEstateList);
        rcListEstate.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rcListEstate.setHasFixedSize(true);
        //Call AsyncTask to retrieve all estates from room database
        getAllEstates getAllEstates = new getAllEstates();
        getAllEstates.execute();

        return v;
    }

    class getAllEstates extends AsyncTask<Void, Void, List<Estate>> {

        @Override
        protected List<Estate> doInBackground(Void... voids) {

            List<Estate> estateList = DbHelper.getInstance(requireActivity()).getAppDatabase().estateDao().getEstates();
            return estateList;
        }

        @Override
        protected void onPostExecute(List<Estate> aVoid) {
            super.onPostExecute(aVoid);

            EstateAdapter estateAdapter = new EstateAdapter(requireActivity(), aVoid, position -> Toast.makeText(requireContext(), "Clicked at : " + position, Toast.LENGTH_SHORT).show());
            rcListEstate.setAdapter(estateAdapter);
            estateAdapter.notifyDataSetChanged();
            Toast.makeText(requireActivity(), "All " + aVoid.size() + " Estates retrieved", Toast.LENGTH_LONG).show();
        }
    }
}
