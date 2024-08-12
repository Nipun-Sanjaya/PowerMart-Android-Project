package lk.zeamac.app.powermart.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.zeamac.app.powermart.R;

public class OrderInfoActivity extends AppCompatActivity {

    Button buttonDelivery,buttonConfirm;
    LinearLayout linearLayout;

    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;

    TextView address,addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        buttonDelivery = findViewById(R.id.btnDelivery);
        linearLayout = findViewById(R.id.linearLayout);
        buttonConfirm = findViewById(R.id.btnConfirm);
        address = findViewById(R.id.deliveryText);
        addNew = findViewById(R.id.addNewAddressText);

        buttonDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDelivery.setBackgroundColor(Color.rgb(85, 85, 84));
                linearLayout.setVisibility(View.VISIBLE);
                buttonConfirm.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
                addNew.setVisibility(View.VISIBLE);
            }
        });


        findViewById(R.id.addNewAddressText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderInfoActivity.this, AddDeliveryAddressActivity.class);
                startActivity(intent);
            }
        });

        //check the user available

        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null){
                    fireStore.collection("UsersAddress").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            DocumentSnapshot snapshot = task.getResult();

                            if(snapshot.exists()){
                                Intent intent = new Intent(OrderInfoActivity.this, DeliveryLocationActivity.class );
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(OrderInfoActivity.this, "Please Add Your Address", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        //check the user available

        findViewById(R.id.btnStore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderInfoActivity.this, StoreMapActivity.class );
                startActivity(intent);
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderInfoActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });

    }
}