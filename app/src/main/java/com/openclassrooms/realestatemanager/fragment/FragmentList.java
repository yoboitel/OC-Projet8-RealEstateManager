package com.openclassrooms.realestatemanager.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapter.EstateAdapter;

import java.util.List;

public class FragmentList extends Fragment {

    RecyclerView rcListEstate;
    LinearLayout viewNoEstates;
    FrameLayout frameDetailTablet;
    boolean tabletMode = false;

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

        //Check if we are in tablet mode to display the master/detail layout
        frameDetailTablet = v.findViewById(R.id.frameDetailTablet);
        if (frameDetailTablet != null) {
            //We are in tablet mode so display the fragment of the estate's detail in the right layout
            tabletMode = true;
        }

        viewNoEstates = v.findViewById(R.id.viewNoEstates);
        //Setup recyclerview
        rcListEstate = v.findViewById(R.id.rcEstateList);
        rcListEstate.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rcListEstate.setHasFixedSize(true);
        //Call AsyncTask to retrieve all estates from room database
        getAllEstates getAllEstates = new getAllEstates();
        getAllEstates.execute();

        return v;
    }

    //Refresh the list just after a new estate is added from the NewEstateActivity.java
    @Override
    public void onResume() {
        super.onResume();
        new getAllEstates().execute();
    }

    private void displayListOfEstates(List<Estate> aVoid) {
        EstateAdapter estateAdapter = new EstateAdapter(requireActivity(), aVoid, position -> {
            if (tabletMode) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameDetailTablet, new FragmentDetail(aVoid.get(position).getId()))
                        .addToBackStack(null)
                        .commit();
            } else {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, new FragmentDetail(aVoid.get(position).getId()))
                        .addToBackStack(null)
                        .commit();
            }
        });
        rcListEstate.setAdapter(estateAdapter);
        estateAdapter.notifyDataSetChanged();
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

            if (aVoid.isEmpty())
                viewNoEstates.setVisibility(View.VISIBLE);
            else {
                viewNoEstates.setVisibility(View.GONE);
                displayListOfEstates(aVoid);
            }
        }
    }
}
