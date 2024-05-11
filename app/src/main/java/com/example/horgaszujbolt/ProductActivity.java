package com.example.horgaszujbolt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mauth;
    private RecyclerView pRecyclerView;
    private ArrayList<Product> pProductList;
    private ProductAdapter pAdapter;
    private int gridnumber = 1;
    private int asd = 0;

    private FirebaseFirestore pFirestore;
    private CollectionReference pProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        NavigationManager navigationManager = new NavigationManager(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.product);
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

        mauth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }
        pRecyclerView = findViewById(R.id.productRecyclerView);
        pRecyclerView.setLayoutManager(new GridLayoutManager(this, gridnumber));
        pProductList = new ArrayList<>();

        pAdapter = new ProductAdapter(this, pProductList);
        pRecyclerView.setAdapter(pAdapter);

        pFirestore = FirebaseFirestore.getInstance();
        pProducts = pFirestore.collection("Products");

        querydata();
    }

    private void querydata(){
        pProductList.clear();

        pProducts.limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                Product product = document.toObject(Product.class);
                pProductList.add(product);
            }
            if (pProductList.size() == 0){
                initializeData();
                querydata();
            }

            pAdapter.notifyDataSetChanged();
        });

    }
    private void initializeData(){
        String[] productInfo = getResources().getStringArray(R.array.product_desc);
        String[] productPrice = getResources().getStringArray(R.array.product_price);
        TypedArray productImage = getResources().obtainTypedArray(R.array.product_images);
        
        pProductList.clear();

        for (int i = 0; i < productInfo.length; i++) {
            pProducts.add(new Product(productInfo[i], productPrice[i],productImage.getResourceId(i, 0)));
        }
        productImage.recycle();

        pAdapter.notifyDataSetChanged();
    }
}