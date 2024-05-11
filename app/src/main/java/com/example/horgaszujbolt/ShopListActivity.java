package com.example.horgaszujbolt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ShopListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private FirebaseUser user;

    private int gridNumber = 1;

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<ShopingItem> mItemsData;
    private ShopingItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private SharedPreferences preferences;

    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        NavigationManager navigationManager = new NavigationManager(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.rods);
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



        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }


        // recycle view
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));
        mItemsData = new ArrayList<>();
        mAdapter = new ShopingItemAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");

        queryData();
    }

    private void queryData(){
        mItemsData.clear();
        mItems.orderBy("name").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                ShopingItem  item = document.toObject(ShopingItem.class);
                mItemsData.add(item);
            }
            if (mItemsData.size() == 0){
                initializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        String[] itemsList = getResources()
                .getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources()
                .getStringArray(R.array.shopping_item_price);
        TypedArray itemsImageResources =
                getResources().obtainTypedArray(R.array.shopping_item_images);


        for (int i = 0; i < itemsList.length; i++)
            mItems.add(new ShopingItem(itemsList[i], itemsInfo[i], itemsPrice[i],
                    itemsImageResources.getResourceId(i, 0)));


        itemsImageResources.recycle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryData();
    }

    private void deleteItem(ShopingItem item) {
        mItems.document(item.getName()).delete()
                .addOnSuccessListener(aVoid -> {
                    mItemsData.remove(item);
                    mAdapter.notifyDataSetChanged();
                    Log.d(LOG_TAG, "Item deleted successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(LOG_TAG, "Error deleting item", e);
                });
    }
    private void updateItem(ShopingItem item) {
        mItems.document(item.getName()).set(item)
                .addOnSuccessListener(aVoid -> {
                    int index = mItemsData.indexOf(item);
                    if (index != -1) {
                        mItemsData.set(index, item);
                        mAdapter.notifyDataSetChanged();
                        Log.d(LOG_TAG, "Item updated successfully");
                    } else {
                        Log.e(LOG_TAG, "Item not found in local list");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(LOG_TAG, "Error updating item", e);
                });
    }

}