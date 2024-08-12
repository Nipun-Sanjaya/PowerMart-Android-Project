package lk.zeamac.app.powermart.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Adapter.OrderAdapter;
import lk.zeamac.app.powermart.Entity.OrderEntity;
import lk.zeamac.app.powermart.R;

public class OrderViewActivity extends AppCompatActivity {
    private RecyclerView.Adapter orderAdapter ;
    private RecyclerView recyclerOrderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oreder_view);

        ArrayList<OrderEntity> items = new ArrayList<>();
        items.add(new OrderEntity("1","chainsaw","Chainsaw","1000.00","Pending","30 jun 2023"));
        items.add(new OrderEntity("2","grainder","Grainder","1500.00","Progress","30 jun 2023"));
        items.add(new OrderEntity("3","generators","Generators","2000.00","Accepted","30 jun 2023"));
        items.add(new OrderEntity("4","grainder2","Grainder","1500.00","Pending","30 jun 2023"));



        recyclerOrderView = findViewById(R.id.OrderView);
        recyclerOrderView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        orderAdapter = new OrderAdapter(items);
        recyclerOrderView.setAdapter(orderAdapter);

        findViewById(R.id.orderBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderViewActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }
}