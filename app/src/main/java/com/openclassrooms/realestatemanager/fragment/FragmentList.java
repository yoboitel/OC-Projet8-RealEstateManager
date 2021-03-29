package com.openclassrooms.realestatemanager.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.Database.DbHelper;
import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapter.EstateAdapter;

import java.util.List;

public class FragmentList extends Fragment {

    RecyclerView rcListEstate;
    LinearLayout viewNoEstates;

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
                EstateAdapter estateAdapter = new EstateAdapter(requireActivity(), aVoid, new EstateAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, new FragmentDetail(aVoid.get(position).getId()))
                                .addToBackStack(null).commit();
                    }
                });
                rcListEstate.setAdapter(estateAdapter);
                estateAdapter.notifyDataSetChanged();
            }
        }
    }

    //Refresh the list just after a new estate is added from the NewEstateActivity.java
    @Override
    public void onResume() {
        super.onResume();
        new getAllEstates().execute();
    }
}
