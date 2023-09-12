package com.jakop52.apartmentrent.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakop52.apartmentrent.android.activities.AddApartmentActivity;
import com.jakop52.apartmentrent.android.activities.ApartmentDetailsActivity;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.adapters.ApartmentAdapter;
import com.jakop52.apartmentrent.android.dto.ApartmentDto;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.ApartmentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnedApartmentsFragment extends Fragment {
    private static final int REQUEST_CODE_ADD_APARTMENT = 1;

    private RecyclerView recyclerViewOwnedApartments;
    private ApartmentService apartmentService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apartments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewOwnedApartments = view.findViewById(R.id.recyclerViewApartments);
        recyclerViewOwnedApartments.setLayoutManager(new LinearLayoutManager(getContext()));

        apartmentService = ApiClient.getClient().create(ApartmentService.class);

        fetchOwnedApartments();

        FloatingActionButton fabAddApartment = view.findViewById(R.id.fabAddApartment);
        fabAddApartment.setVisibility(View.VISIBLE);
        fabAddApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddApartmentActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_APARTMENT);
            }
        });
    }

    private void fetchOwnedApartments() {
        String sessionId = PreferencesManager.getInstance().getSessionId();

        if (sessionId != null) {
            Call<List<ApartmentDto>> call = apartmentService.getMyApartments(sessionId);

            call.enqueue(new Callback<List<ApartmentDto>>() {
                @Override
                public void onResponse(Call<List<ApartmentDto>> call, Response<List<ApartmentDto>> response) {
                    if (response.isSuccessful()) {
                        List<ApartmentDto> myApartments = response.body();
                        ApartmentAdapter adapter = new ApartmentAdapter(myApartments);
                        recyclerViewOwnedApartments.setAdapter(adapter);
                        adapter.setOnItemClickListener(new ApartmentAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(ApartmentDto apartment) {
                                Intent intent = new Intent(getContext(), ApartmentDetailsActivity.class);
                                intent.putExtra("apartmentId", apartment.getId());
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Wystąpił błąd podczas ładowania danych.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ApartmentDto>> call, Throwable t) {
                    Toast.makeText(getContext(), "Nie udało się połączyć z serwerem.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_APARTMENT && resultCode == Activity.RESULT_OK) {
            fetchOwnedApartments();
        }
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(getContext(), targetActivity);
        startActivity(intent);
    }
}
