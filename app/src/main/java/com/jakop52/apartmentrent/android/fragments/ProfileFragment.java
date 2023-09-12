package com.jakop52.apartmentrent.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.activities.LoginActivity;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.UserDto;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.AuthService;
import com.jakop52.apartmentrent.android.services.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private String sessionId = PreferencesManager.getInstance().getSessionId();
    private TextView textViewGreeting;
    private UserDto user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnLogout = view.findViewById(R.id.buttonLogout);
        textViewGreeting = view.findViewById(R.id.textViewGreeting);
        CardView cardViewReservationsButton = view.findViewById(R.id.myReservationsCardViewButton);

        cardViewReservationsButton.setOnClickListener(view1 -> showUserReservationsList());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });

        setUserHello();
    }

    private void showUserReservationsList() {
        UserReservationListFragment userReservationListFragment = new UserReservationListFragment(getContext());
        userReservationListFragment.show(getChildFragmentManager(), userReservationListFragment.getTag());
    }

    private void performLogout() {
        AuthService authService = ApiClient.getClient().create(AuthService.class);
        Call<Void> call = authService.logout();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    PreferencesManager.getInstance().clearSession();

                    navigateTo(LoginActivity.class);
                } else {
                    Toast.makeText(getContext(), "Nie udało się wylogować.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Nie udało się połączyć z serwerem.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setUserHello(){
        Call<UserDto> userDtoCall = ApiClient.getClient().create(UserRepository.class).getUserData(sessionId);
        userDtoCall.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if(response.isSuccessful()){
                    textViewGreeting.setText(textViewGreeting.getText()+" "+response.body().getUsername()+"!");
                    user=response.body();
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {

            }
        });
    }
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(getContext(), targetActivity);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
