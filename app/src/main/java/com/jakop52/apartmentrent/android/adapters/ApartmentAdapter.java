package com.jakop52.apartmentrent.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.ApartmentDto;
import com.jakop52.apartmentrent.android.model.Media;
import com.jakop52.apartmentrent.android.services.MediaService;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder> {

    private List<ApartmentDto> apartments;
    private OnItemClickListener clickListener;
    public ApartmentAdapter(List<ApartmentDto> apartments) {
        this.apartments = apartments;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(ApartmentDto apartment);
    }

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewApartmentName,textViewDescription,textViewRent;
        private ImageView imageView;

        public ApartmentViewHolder(View itemView) {
            super(itemView);
            textViewApartmentName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewRent = itemView.findViewById(R.id.textViewRent);
            imageView = itemView.findViewById(R.id.imageViewApartment);
        }
    }

    @NonNull
    @Override
    public ApartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apartment, parent, false);
        return new ApartmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentViewHolder holder, int position) {
        ApartmentDto apartment = apartments.get(position);
        holder.textViewApartmentName.setText(apartment.getName());
        holder.textViewDescription.setText(apartment.getDescription());
        holder.textViewRent.setText(apartment.getRent().toString());
        setFirstMediaImage(apartment, holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(apartment);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    public void setFirstMediaImage(ApartmentDto apartment, ImageView imageView) {
        if (apartment.getMedia() != null && !apartment.getMedia().isEmpty()) {
            Media firstMedia = apartment.getMedia().iterator().next();

            Long mediaId = firstMedia.getId();

            MediaService mediaService = ApiClient.getClient().create(MediaService.class);
            Call<ResponseBody> call = mediaService.downloadMediaFile(mediaId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();

                        try{
                            Glide.with(imageView.getContext())
                                    .load(responseBody.bytes())
                                    .apply(new RequestOptions().centerCrop())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(imageView);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    imageView.setImageResource(R.drawable.placeholder_image);
                }
            });
        } else {
            imageView.setImageResource(R.drawable.placeholder_image);
        }
    }

}

