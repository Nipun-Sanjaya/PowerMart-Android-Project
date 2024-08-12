package lk.zeamac.app.powermart.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import lk.zeamac.app.powermart.R;

public class PaymentActivity extends AppCompatActivity  {

    String tPrice = CheckoutActivity.totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView textView = findViewById(R.id.textView12);
        textView.setText(tPrice);


        findViewById(R.id.pur_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "Payment Is Successful", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.payment_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, CheckoutActivity.class );
                startActivity(intent);
            }
        });
    }
}