package com.openclassrooms.realestatemanager.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;

import java.io.File;
import java.util.List;

public class EstateAdapter extends RecyclerView.Adapter<EstateAdapter.ViewHolder> {

    private Context context;
    private List<Estate> list;
    private OnItemClickListener onItemClickListener;

    public EstateAdapter(Context context, List<Estate> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final Estate model, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(getLayoutPosition()));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_estate, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Estate item = list.get(position);

        holder.bind(item, onItemClickListener);

        //Link Views
        TextView tvTypeEstate = holder.itemView.findViewById(R.id.tvEstateType);
        TextView tvAddressEstate = holder.itemView.findViewById(R.id.tvEstateAddress);
        TextView tvPriceEstate = holder.itemView.findViewById(R.id.tvEstatePrice);
        ImageView ivPhotoEstate = holder.itemView.findViewById(R.id.ivEstatePic);
        ImageView ivEstateSold = holder.itemView.findViewById(R.id.ivEstateSold);

        //Set Texts
        tvTypeEstate.setText(item.getType());
        tvAddressEstate.setText(item.getAddress());
        tvPriceEstate.setText(String.format("$%s", item.getPrice()));
        ivPhotoEstate.setImageURI(Uri.parse(item.getPhotoUrls().get(0)));
        //Show sold icon if estate is sold
        if (item.getDateSold() != null)
            ivEstateSold.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}