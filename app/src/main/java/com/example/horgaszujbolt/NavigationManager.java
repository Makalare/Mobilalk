package com.example.horgaszujbolt;

import android.content.Context;
import android.content.Intent;

public class NavigationManager {
    private Context context;

    public NavigationManager(Context context) {
        this.context = context;
    }

    public void product() {
        Intent intent = new Intent(context, ProductActivity.class);
        context.startActivity(intent);
    }
    public void rods() {
        Intent intent = new Intent(context, ShopListActivity.class);
        context.startActivity(intent);
    }
    public void log_out() {
        Intent intent = new Intent(context, LogOutActivity.class);
        context.startActivity(intent);
    }
}