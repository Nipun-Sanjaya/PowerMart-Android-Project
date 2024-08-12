package lk.zeamac.app.powermart.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.zeamac.app.powermart.Adapter.ProductViewAdapter;
import lk.zeamac.app.powermart.Adapter.SimilarProductAdapter;
import lk.zeamac.app.powermart.Entity.CartEntity;
import lk.zeamac.app.powermart.Entity.ProductViewEntity;
import lk.zeamac.app.powermart.Entity.SimilarProductEntity;
import lk.zeamac.app.powermart.R;

public class SingleProductViewActivity extends AppCompatActivity implements ProductViewAdapter.OnProductClickListener {

    private RecyclerView.Adapter similarProductAdapter ;
    private RecyclerView recyclerView;
    private FirebaseStorage storage;
    TextView title, description, price, qty, totalPrice, increase, decrease, updateQty,tPrice;
    double productPrice,amount;
    ImageView image;
    String categoryName,productId, queryId;
    int count = 1;
    private FirebaseFirestore fireStore;
    int updatedValue;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ProductViewEntity> productItems;
    private ProductViewAdapter productAdapter;
    private RecyclerView recyclerViewProductView;
    Long fixedPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_view);

        storage = FirebaseStorage.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId");
            //loadProduct(productId);
            OnProductClick(productId);
        }

//        ArrayList<SimilarProductEntity> items = new ArrayList<>();
//        items.add(new SimilarProductEntity("power_tools","Power Tools"));
//        items.add(new SimilarProductEntity("machinery_post","Machinery"));
//        items.add(new SimilarProductEntity("agricultural_equipment","Agricultural Equipment"));
//        items.add(new SimilarProductEntity("air_tools","Air Tools"));
//        items.add(new SimilarProductEntity("construction_equipment","Construction Equipment"));
//        items.add(new SimilarProductEntity("hand_tools","Hand Tools"));
//        items.add(new SimilarProductEntity("measuring_equipment","Measuring Equipment"));
//        items.add(new SimilarProductEntity("spare_part","Spare Part"));
//
//        recyclerView = findViewById(R.id.similarView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
//        similarProductAdapter = new SimilarProductAdapter(items);
//        recyclerView.setAdapter(similarProductAdapter);


        //similar Product

        productItems = new ArrayList<>();
        recyclerViewProductView = findViewById(R.id.similarView);
        recyclerViewProductView.setLayoutManager(new GridLayoutManager(SingleProductViewActivity.this, 2, GridLayoutManager.VERTICAL, false));
        productAdapter = new ProductViewAdapter(productItems, SingleProductViewActivity.this, this::OnProductClick);
        recyclerViewProductView.setAdapter(productAdapter);


        fireStore.collection("Products")
                //                .limit(5)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange change : value.getDocumentChanges()) {
                            ProductViewEntity product = change.getDocument().toObject(ProductViewEntity.class);
                            switch (change.getType()) {
                                case ADDED:
                                    productItems.add(product);
                                case MODIFIED:
                                    ProductViewEntity old = productItems.stream().filter(i -> i.getId()
                                                    .equals(product.getId()))
                                            .findFirst()
                                            .orElse(null);


                                    if (old != null) {
                                        old.setName(product.getName());
                                        old.setDescription(product.getDescription());
                                        old.setCategory(product.getCategory());
                                        old.setQty(product.getQty());
                                        old.setPrice(product.getPrice());
                                        old.setImage(product.getImage());
                                    }
                                    break;
                                case REMOVED:
                                    productItems.remove(product);
                            }
                        }

                        productAdapter.notifyDataSetChanged();

                    }
                });

        //Add to Cart

        findViewById(R.id.addCartbtn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long totalQty = Long.parseLong(String.valueOf(count));


                DocumentReference reference = fireStore.collection("Cart").document(queryId);
                reference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot productDoc = task.getResult();
                        if(productDoc.exists()){
                            Toast.makeText(SingleProductViewActivity.this, "Already added! Quantity Updated", Toast.LENGTH_SHORT).show();
                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("qty",  totalQty);

                            reference.update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        }else{
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();

                            DocumentReference reference1 = fireStore.collection("Cart")
                                    .document(queryId);

                            CartEntity cart = new CartEntity(queryId,queryId,userId,totalQty,fixedPrice,false);


                            reference1.set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SingleProductViewActivity.this, "Add To Cart Successful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SingleProductViewActivity.this, "Add To Cart Failed", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }



                });

            }
        });


        //Add to Cart

        //similar Product

        findViewById(R.id.cartCheckOutbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SingleProductViewActivity.this, "Please, Add your product cart and buy it.", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.single_product_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleProductViewActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }

    private void OnCategorySearch(String categoryType) {

        ArrayList<ProductViewEntity> filteredList = new ArrayList<>();
        for (ProductViewEntity product : productItems) {

            if (product.getCategory().equals(categoryType)) {
                filteredList.add(product);
            }
            productAdapter.setFilterSearchList(filteredList);
        }

    }
    @Override
    public void OnProductClick(String product) {
        queryId = product;
        loadProduct(product);
        count = 1;
    }

    public void loadProduct(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Products").whereEqualTo("id", productId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override


            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ProductViewEntity product = document.toObject(ProductViewEntity.class);

                        title = findViewById(R.id.textViewTitle);
                        description = findViewById(R.id.textDescription);
                        price = findViewById(R.id.priceText);
                        qty = findViewById(R.id.availableQty);
                        image = findViewById(R.id.productImage);
                        totalPrice = findViewById(R.id.total_fee1);
                        increase = findViewById(R.id.textViewIncrease);
                        decrease = findViewById(R.id.textViewDecrease);
                        updateQty = findViewById(R.id.textViewUpdateQty);
                        tPrice = findViewById(R.id.textView12);


                        updatedValue = Integer.parseInt(product.getQty());
                        productPrice = Double.parseDouble(product.getPrice());
                        Double d = Double.parseDouble(product.getPrice());
                        fixedPrice = d.longValue();

                        //amount = Double.parseDouble(product.getPrice());
                       // fixedPrice = Integer.valueOf(product.getPrice());


                        totalPrice.setText("" + productPrice);
                        updateQty.setText("1");
                        tPrice.setText("" + productPrice);

                        increase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (updatedValue > count) {
                                    count++;
                                    totalPrice.setText("" + count * productPrice);
                                    tPrice.setText("" + count * productPrice);
                                    updateQty.setText("" + count);


                                } else {
                                    count = updatedValue;
                                }
                            }
                        });
                        decrease.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (count <= 1) {
                                    count = 1;
                                } else {
                                    count--;
                                    updateQty.setText("" + count);
                                    totalPrice.setText("" + count * productPrice);
                                    tPrice.setText("" + count * productPrice);


                                }

                            }
                        });
                        storage.getReference("product-images/" + product.getImage())
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get()
                                                .load(uri)
                                                .resize(200, 200)
                                                .centerCrop()
                                                .into(image);
                                    }
                                });

                        title.setText(product.getName());
                        description.setText(product.getDescription());
                        price.setText(product.getPrice());
                        qty.setText(product.getQty());
                        categoryName = product.getCategory();

                        OnCategorySearch(categoryName);

                    }
                } else {
                    Log.w("FireStore", "Error getting documents", task.getException());
                }
            }
        });

    }
}