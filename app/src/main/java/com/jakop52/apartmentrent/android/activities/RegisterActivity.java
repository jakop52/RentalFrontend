package com.jakop52.apartmentrent.android.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.RegisterRequest;
import com.jakop52.apartmentrent.android.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String repeatPassword = editTextRepeatPassword.getText().toString();

                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repeatPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                AuthService authService = ApiClient.getClient().create(AuthService.class);
                Call<Void> call = authService.register(new RegisterRequest(userName,email,password));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Registration successful! Please log in.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error: "+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
