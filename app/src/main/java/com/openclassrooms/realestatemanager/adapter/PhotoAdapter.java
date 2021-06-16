package com.openclassrooms.realestatemanager.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private List<String> listPhotoUrls;
    private List<String> listPhotoDescriptions;
    private boolean showTrashIcon;

    public PhotoAdapter(Context context, List<String> listPhotoUrls, List<String> listPhotoDescriptions, boolean ShowTrashIcon) {
        this.context = context;
        this.listPhotoUrls = listPhotoUrls;
        this.listPhotoDescriptions = listPhotoDescriptions;
        this.showTrashIcon = ShowTrashIcon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String itemUrl = listPhotoUrls.get(position);
        String itemDesc = listPhotoDescriptions.get(position);

        //Setup viewholder views
        ImageView ivPhoto = holder.itemView.findViewById(R.id.ivPhotoBackground);
        TextView tvPhotoText = holder.itemView.findViewById(R.id.ivPhotoText);
        tvPhotoText.setSelected(true);
        ImageView ivDeletePhoto = holder.itemView.findViewById(R.id.ivDeletePhoto);
        if (!showTrashIcon)
            ivDeletePhoto.setVisibility(View.GONE);

        //Set photo image and text
        ivPhoto.setImageURI(Uri.parse(itemUrl));
        tvPhotoText.setText(itemDesc);

        //Remove photo when trash icon clicked
        ivDeletePhoto.setOnClickListener(view -> {
            listPhotoUrls.remove(holder.getAdapterPosition());
            listPhotoDescriptions.remove(holder.getAdapterPosition());
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listPhotoUrls.size());
            notifyItemRangeChanged(position, listPhotoDescriptions.size());
        });
    }

    @Override
    public int getItemCount() {
        return listPhotoUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}