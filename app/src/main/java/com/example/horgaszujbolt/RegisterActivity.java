package com.example.horgaszujbolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.function.LongFunction;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    EditText userEmailEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordAgainEditText);

        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        String email = userEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Kérlek add meg az e-mail címed", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Kérlek add meg a jelszavad", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "Sikeres Regisztráció!");
                    startShopping();
                }else{
                    Log.w(LOG_TAG,"Sikertelen Regisztráció!");
                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció (A jelszó minimum 6 karakter kell hogy legyen)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void cancel(View view) {
        finish();
    }

    private void startShopping(){
        Intent intent = new Intent(this, ShopListActivity.class);
        startActivity(intent);
    }

}