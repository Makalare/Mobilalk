package com.example.horgaszujbolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    EditText userEmailET;
    EditText passwordET;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            Log.d(LOG_TAG, "Authenticated User!");
        }else {
            Log.d(LOG_TAG, "Unauthenticated User!");
        }

        userEmailET = findViewById(R.id.userEmailEditText);
        passwordET = findViewById(R.id.passwordEditText);

        mAuth = FirebaseAuth.getInstance();
    }

    public void registration(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String email = userEmailET.getText().toString();
        String password = passwordET.getText().toString();

        // Ellenőrizzük, hogy mindkét mező ki van-e töltve
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Töltse ki a mezőket!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "Sikeres bejelentkezés!");
                            startShopping();
                        } else {
                            Toast.makeText(MainActivity.this, "Hibás jelszó vagy Email-cím!", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Sikertelen bejelentkezés!");
                        }
                    }
                });
    }


    private void startShopping(){
        Intent intent = new Intent(this, ShopListActivity.class);
        startActivity(intent);
    }
    public void loginAsGuest(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "Sikeres bejelentkezés vendégként!");
                    startShopping();
                }else{
                    Log.d(LOG_TAG,"Sikertelen bejelentkezés!");
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }



}