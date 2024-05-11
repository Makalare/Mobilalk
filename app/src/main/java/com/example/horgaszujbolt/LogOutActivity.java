package com.example.horgaszujbolt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogOutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        TextView emailTextView = findViewById(R.id.emailTextView);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            emailTextView.setText("Felhasználó e-mail címe: " + userEmail);
        } else {
            emailTextView.setText("Nincs bejelentkezett felhasználó.");
        }


        NavigationManager navigationManager = new NavigationManager(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.log_out);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.rods) {
                navigationManager.rods();
                return true;
            } else if (item.getItemId() == R.id.product) {
                navigationManager.product();
                return true;
            } else if (item.getItemId() == R.id.log_out) {
                navigationManager.log_out();
                return true;
            }
            return false;
        });


    }

    public void logout(View view) {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}