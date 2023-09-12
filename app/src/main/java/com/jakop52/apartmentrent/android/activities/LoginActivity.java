package com.jakop52.apartmentrent.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.LoginRequest;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {
    AuthService authService;
    private EditText etUsername;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        etUsername = findViewById(R.id.editTextUsernameOrEmail);
        etPassword = findViewById(R.id.editTextPassword);

        Button btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Wprowadź nazwę użytkownika i hasło.", Toast.LENGTH_SHORT).show();
                } else {
                    performLogin(username, password);
                }
            }
        });
        Button btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        authService = ApiClient.getClient().create(AuthService.class);
    }


    private void performLogin(String username, String password) {
        Call<Void> call = authService.login(new LoginRequest(username, password));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String jsessionid = response.headers().get("Set-Cookie");
                    Log.d("LoginDTO callback: ", "Success || " + jsessionid);
                    PreferencesManager.getInstance().setSessionId(jsessionid);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (response.code() == 401) {
                    Log.d("LoginDTO callback: ", "Invalid login data");
                    Toast.makeText(getApplicationContext(), "Login or password incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("LoginDTO callback: ", "unknown error || CODE: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Nie udało się połączyć z serwerem.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
    private void navigateTo(Class<?> destinationActivity) {
        Intent intent = new Intent(this, destinationActivity);
        startActivity(intent);
        finish();
    }
}