package lk.zeamac.app.powermart.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Adapter.CheckoutAdapter;
import lk.zeamac.app.powermart.Adapter.OrderAdapter;
import lk.zeamac.app.powermart.Entity.CartEntity;
import lk.zeamac.app.powermart.Entity.CheckoutEntity;
import lk.zeamac.app.powermart.Entity.OrderEntity;
import lk.zeamac.app.powermart.Entity.ProfileLocationInfoEntity;
import lk.zeamac.app.powermart.R;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView.Adapter checkoutAdapter ;
    private RecyclerView recyclerCheckoutView;
    private ArrayList<CartEntity> productItems;
    private RecyclerView recyclerViewProductView;
    private CheckoutAdapter cartViewAdapter;
    private FirebaseFirestore fireStore;
    TextView totalProductFee, itemTotal,increaseDay,decreaseDay,updateDay;
    public static String totalPrice;

    double totalAmount, changeAmount;
    private FirebaseAuth firebaseAuth;

    int count = 1;
    int updatedValue = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        totalProductFee = findViewById(R.id.totalPr);
        itemTotal = findViewById(R.id.itemTotal1);
        increaseDay = findViewById(R.id.textIncreaseQty1);
        decreaseDay = findViewById(R.id.textDecreaseQty1);
       // DeliveryFee = findViewById(R.id.textDeliveryFee);
        updateDay = findViewById(R.id.textDayQty);




        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            CollectionReference cartRef = fireStore.collection("Cart");
            Query query = cartRef.whereEqualTo("userId", currentUser.getUid());
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String cartSize = String.valueOf(queryDocumentSnapshots.size());
                    if (!queryDocumentSnapshots.isEmpty()) {
                        productItems = new ArrayList<>();
                        recyclerViewProductView = findViewById(R.id.checkoutView);
                        recyclerViewProductView.setLayoutManager(new GridLayoutManager(CheckoutActivity.this, 1, GridLayoutManager.VERTICAL, false));
                        cartViewAdapter = new CheckoutAdapter(productItems, CheckoutActivity.this);
                        recyclerViewProductView.setAdapter(cartViewAdapter);
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            CartEntity cart = change.getDocument().toObject(CartEntity.class);
                            itemTotal.setText(cartSize);
                            switch (change.getType()) {
                                case ADDED:
                                    productItems.add(cart);
                                case MODIFIED:
                                    CartEntity old = productItems.stream().filter(i -> i.getId().equals(cart.getId())).findFirst().orElse(null);


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference cartRef = db.collection("Cart");
                                    cartRef.whereEqualTo("id", cart.getId()).whereEqualTo("selected", false).get()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    for (DocumentSnapshot document : task1.getResult()) {
                                                        CartEntity item = document.toObject(CartEntity.class);
                                                        totalAmount = 0;
                                                        totalAmount += item.getCartProductFixedPrice() * old.getQty();

                                                    }
                                                    changeAmount += totalAmount;
//                                                    totalPrice = String.valueOf(changeAmount);
//                                                    totalProductFee.setText(totalPrice);

                                                    //All Price
                                                    TextView deliveryFee = findViewById(R.id.textDeliveryFee);
                                                    deliveryFee.setText("500");
                                                    FirebaseUser user1 = firebaseAuth.getCurrentUser();
                                                    String Id = user1.getUid();
                                                    fireStore.collection("UsersAddress").document(Id).get().addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot snapshot = task.getResult();

                                                            if (snapshot.exists()) {
                                                                ProfileLocationInfoEntity address = snapshot.toObject(ProfileLocationInfoEntity.class);
                                                                if (address != null) {
                                                                        deliveryFee.setText("500");
                                                                        int deliveryFeeValue = 500;
                                                                        totalPrice = String.valueOf(count * changeAmount + deliveryFeeValue);
                                                                          totalProductFee.setText(totalPrice);
                                                                    }
                                                                } else {
                                                                    deliveryFee.setText("500");
                                                                }
                                                            }

                                                    });
                                                    //All Price


                                                    //day Add

                                                    increaseDay.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (updatedValue > count) {
                                                                count++;
                                                                int deliveryFeeValue = 500;
                                                                totalPrice = String.valueOf( count * changeAmount + deliveryFeeValue);
                                                                totalProductFee.setText(totalPrice);


                                                                updateDay.setText("" + count);




                                                            } else {
                                                                count = updatedValue;
                                                            }
                                                        }
                                                    });
                                                    decreaseDay.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (count <= 1) {
                                                                count = 1;
                                                            } else {
                                                                count--;
                                                                updateDay.setText("" + count);
                                                                int deliveryFeeValue = 500;
                                                                totalPrice = String.valueOf( count * changeAmount + deliveryFeeValue);
                                                                totalProductFee.setText(totalPrice);

                                                            }

                                                        }
                                                    });

                                                    //day Add



                                                    cartViewAdapter.notifyDataSetChanged();
                                                } else {
                                                }
                                            });

                                    if (old != null) {
                                        old.setQty(cart.getQty());
                                        old.setCartProductFixedPrice(cart.getCartProductFixedPrice());


                                    }
                                    break;
                                case REMOVED:
                                    productItems.remove(cart);
                            }
                        }
                        cartViewAdapter.notifyDataSetChanged();
                    }
                }
            });
        }




//        ArrayList<CheckoutEntity> items = new ArrayList<>();
//        items.add(new CheckoutEntity("grainder","Grainder","10000.00","5"));
//        items.add(new CheckoutEntity("grainder","Grainder","10000.00","5"));
//        items.add(new CheckoutEntity("grainder","Grainder","10000.00","5"));
//        items.add(new CheckoutEntity("grainder","Grainder","10000.00","5"));
//
//        recyclerCheckoutView = findViewById(R.id.checkoutView);
//        recyclerCheckoutView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
//        checkoutAdapter = new CheckoutAdapter(items);
//        recyclerCheckoutView.setAdapter(checkoutAdapter);

        findViewById(R.id.button33).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class );
                startActivity(intent);
            }
        });



        findViewById(R.id.imageViewcheckoutBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }
}