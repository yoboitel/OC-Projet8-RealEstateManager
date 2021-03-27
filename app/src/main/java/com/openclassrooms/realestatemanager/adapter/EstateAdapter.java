package com.openclassrooms.realestatemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class EstateAdapter extends RecyclerView.Adapter<EstateAdapter.ViewHolder> {

    private static final String TAG = EstateAdapter.class.getSimpleName();

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

        View view = inflater.inflate(R.layout.estate_list_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Estate item = list.get(position);

        holder.bind(item, onItemClickListener);

        //Link Views
        TextView tvIdEstate = holder.itemView.findViewById(R.id.tvEstateId);
        TextView tvTypeEstate = holder.itemView.findViewById(R.id.tvEstateType);
        TextView tvPriceEstate = holder.itemView.findViewById(R.id.tvEstatePrice);

        //Set Texts
        tvIdEstate.setText(String.valueOf(item.getId()));
        tvTypeEstate.setText(item.getType());
        tvPriceEstate.setText(item.getPrice());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}