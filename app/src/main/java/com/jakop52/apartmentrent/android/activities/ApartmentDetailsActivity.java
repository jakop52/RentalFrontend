package com.jakop52.apartmentrent.android.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.adapters.MediaViewPagerAdapter;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.ApartmentDto;
import com.jakop52.apartmentrent.android.fragments.RentFragment;
import com.jakop52.apartmentrent.android.fragments.ApartmentManagementFragment;
import com.jakop52.apartmentrent.android.model.Media;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.ApartmentService;
import com.jakop52.apartmentrent.android.services.MediaService;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentDetailsActivity extends AppCompatActivity {

    private ApartmentDto apartment=null;
    private ViewPager2 viewPagerMedia;
    private TextView textViewName, textViewDescription, textViewRent, tvApartmentLocation;
    private Button buttonRent, buttonRentRequestsList, buttonPayments;
    private long apartmentId;
    private ApartmentService apartmentService;
    private MediaService mediaService;
    private List<Object> mediaDrawables = new ArrayList<>();
    private String sessionId = PreferencesManager.getInstance().getSessionId();

    Boolean asOwner, isRentable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);
        apartmentService = ApiClient.getClient().create(ApartmentService.class);
        mediaService = ApiClient.getClient().create(MediaService.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            apartmentId = extras.getLong("apartmentId", -1);
        }

        if (apartmentId != -1) {
            viewPagerMedia = findViewById(R.id.viewPagerMedia);
            textViewName = findViewById(R.id.textViewName);
            textViewDescription = findViewById(R.id.textViewDescription);
            textViewRent = findViewById(R.id.textViewRent);
            tvApartmentLocation = findViewById(R.id.textViewLocation);
            buttonRent = findViewById(R.id.buttonRent);
            buttonRentRequestsList = findViewById(R.id.buttonRentRequests);
            buttonPayments = findViewById(R.id.buttonPayments);
            checkApartmentRentable();
            checkApartmentOwnership();
            getApartmentDetails();
        } else {
        }
    }

    private void getApartmentDetails() {
        Call<ApartmentDto> call = apartmentService.getApartmentDetails(apartmentId);

        call.enqueue(new Callback<ApartmentDto>() {
            @Override
            public void onResponse(Call<ApartmentDto> call, Response<ApartmentDto> response) {
                if (response.isSuccessful()) {
                    apartment = response.body();
                    if (apartment != null) {
                        List<Media> mediaList = apartment.getMedia();
                        loadMediaFiles(mediaList);
                        displayApartmentDetails(apartment);
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ApartmentDto> call, Throwable t) {
            }
        });
    }

    private void loadMediaFiles(List<Media> mediaList) {
        List<Drawable> mediaDrawables = new ArrayList<>();
        for (Media media : mediaList) {
            Long mediaId = media.getId();
            downloadMediaFile(mediaId);
        }
    }
    private void displayApartmentDetails(ApartmentDto apartment) {
        textViewName.setText(apartment.getName());
        textViewDescription.setText(apartment.getDescription());
        textViewRent.setText(apartment.getRent().toString());
        tvApartmentLocation.setText(apartment.getStreet()+" "+apartment.getCity()+" "+apartment.getPostalCode());

        if (isRentable && !asOwner) {
            buttonRent.setVisibility(View.VISIBLE);
            buttonPayments.setVisibility(View.GONE);
            buttonRentRequestsList.setVisibility(View.GONE);
        } else if (isRentable && asOwner) {
            buttonRent.setVisibility(View.GONE);
            buttonPayments.setVisibility(View.GONE);
            buttonRentRequestsList.setVisibility(View.VISIBLE);
        } else {
            buttonRent.setVisibility(View.GONE);
            buttonPayments.setVisibility(View.GONE);
            buttonRentRequestsList.setVisibility(View.GONE);
        }


    }
    private void downloadMediaFile(Long mediaId) {
        Call<ResponseBody> call = mediaService.downloadMediaFile(mediaId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        mediaDrawables.add(drawable);
                        viewPagerMedia.setAdapter(new MediaViewPagerAdapter(ApartmentDetailsActivity.this, mediaDrawables));
                        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
                        dotsIndicator.setViewPager2(viewPagerMedia);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void checkApartmentRentable(){
        Call<Boolean> call = apartmentService.isApartmentRentable(apartmentId, sessionId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    isRentable = response.body();
                } else {
                    isRentable = false;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                isRentable = false;            }
        });
    }

    private void checkApartmentOwnership(){
        Call<Boolean> call = apartmentService.isApartmentOwner(apartmentId, sessionId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    asOwner = response.body();
                } else {
                    asOwner = false;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                asOwner = false;
            }
        });
    }

    private void showRentFragment() {
        RentFragment rentFragment = new RentFragment(apartment,this);
        rentFragment.show(getSupportFragmentManager(), rentFragment.getTag());
    }

    private void showManageApartmentFragment(){
        ApartmentManagementFragment apartmentManagementFragment = new ApartmentManagementFragment(apartment,this);
        apartmentManagementFragment.show(getSupportFragmentManager(), apartmentManagementFragment.getTag());
    }

}
