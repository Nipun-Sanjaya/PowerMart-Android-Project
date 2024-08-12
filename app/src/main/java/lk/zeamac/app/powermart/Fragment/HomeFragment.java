package lk.zeamac.app.powermart.Fragment;

import static lk.zeamac.app.powermart.R.id.container;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Activity.CheckoutActivity;
import lk.zeamac.app.powermart.Activity.LoginActivity;
import lk.zeamac.app.powermart.Activity.MainActivity;
import lk.zeamac.app.powermart.Activity.OrderInfoActivity;
import lk.zeamac.app.powermart.Adapter.BestDealAdapter;
import lk.zeamac.app.powermart.Adapter.CategoryViewAdapter;
import lk.zeamac.app.powermart.Entity.CategoryEntity;
import lk.zeamac.app.powermart.Entity.BestDealItemsEntity;
import lk.zeamac.app.powermart.Entity.UserEntity;
import lk.zeamac.app.powermart.R;


public class HomeFragment extends Fragment implements CategoryViewAdapter.OnCategoryClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;

    private RecyclerView.Adapter categoryAdapter, bestDealAdapter;
    private RecyclerView recyclerViewCategory, recyclerViewBestDeal;

    private ArrayList<CategoryEntity> categoryItems;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;


    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        drawerLayout = getActivity().findViewById(R.id.drawerLayout);
        navigationView = getActivity().findViewById(R.id.navigationView);
        toolbar = fragment.findViewById(R.id.toolBar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        View v = navigationView.getHeaderView(0);
        TextView headerName = v.findViewById(R.id.userHeaderName);
        TextView headerEmail = v.findViewById(R.id.userHeaderEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            fireStore.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        UserEntity user1 = snapshot.toObject(UserEntity.class);
                        if(user1 != null){
                            headerName.setText(user1.getName());
                            headerEmail.setText(user1.getEmail());
                        }
                    }else {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if(firebaseUser != null){
                            headerName.setText(firebaseUser.getDisplayName());
                            headerEmail.setText(firebaseUser.getEmail());
                        }
                    }
                }
            });


        }


        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.open();

            }
        });

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getActivity());

        //category

//        items = new ArrayList<>();
//        items.add(new CategoryEntity("power_tools", "Power Tools"));
//        items.add(new CategoryEntity("machinery_post", "Machinery"));
//        items.add(new CategoryEntity("agricultural_equipment", "Agricultural Equipment"));
//        items.add(new CategoryEntity("air_tools", "Air Tools"));
//        items.add(new CategoryEntity("construction_equipment", "Construction Equipment"));
//        items.add(new CategoryEntity("hand_tools", "Hand Tools"));
//        items.add(new CategoryEntity("measuring_equipment", "Measuring Equipment"));
//        items.add(new CategoryEntity("spare_part", "Spare Part"));
//
//        recyclerViewCategory = fragment.findViewById(R.id.category_home);
//        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        categoryAdapter = new CategoryViewAdapter(items, this::OnCategoryClick);
//        recyclerViewCategory.setAdapter(categoryAdapter);

        categoryItems = new ArrayList<>();
        RecyclerView itemView = fragment.findViewById(R.id.category_home);
        CategoryViewAdapter categoryAdapter = new CategoryViewAdapter(categoryItems, getActivity(), this::OnCategoryClick);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        itemView.setLayoutManager(linearLayoutManager);
        itemView.setAdapter(categoryAdapter);


        fireStore.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange change : value.getDocumentChanges()) {
                    CategoryEntity category = change.getDocument().toObject(CategoryEntity.class);
                    switch (change.getType()) {
                        case ADDED:
                            categoryItems.add(category);
                        case MODIFIED:
                            CategoryEntity old = categoryItems.stream().filter(i -> i.getId()
                                            .equals(category.getId()))
                                    .findFirst()
                                    .orElse(null);


                            if (old != null) {
                                old.setName(category.getName());
                                old.setImagePath(category.getImagePath());
                            }
                            break;
                        case REMOVED:
                            categoryItems.remove(category);
                    }
                }

                categoryAdapter.notifyDataSetChanged();
            }
        });

        //bestDealItems
        ArrayList<BestDealItemsEntity> best_deal_items = new ArrayList<>();
        best_deal_items.add(new BestDealItemsEntity("grainder2", "Angle Grinder", "1000"));
        best_deal_items.add(new BestDealItemsEntity("grasscutter", "Grass Cutter Machine", "1000"));
        best_deal_items.add(new BestDealItemsEntity("chainsaw", "Chain Saw", "1000"));
        best_deal_items.add(new BestDealItemsEntity("generators", "Generators", "1000"));
        best_deal_items.add(new BestDealItemsEntity("grainder2", "Angle Grinder", "1000"));


        recyclerViewBestDeal = fragment.findViewById(R.id.bestView);
        recyclerViewBestDeal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        // recyclerViewBestDeal.setLayoutManager(new GridLayoutManager(getActivity(),2));
        bestDealAdapter = new BestDealAdapter(best_deal_items);
        recyclerViewBestDeal.setAdapter(bestDealAdapter);

        fragment.findViewById(R.id.textInputSearchHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(container, new ProductViewFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        fragment.findViewById(R.id.shop_Now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(container, new ProductViewFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        fragment.findViewById(R.id.deliveryNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });


    }

    @Override
    public void OnCategoryClick(String categoryType, String categoryName) {
        ProductViewFragment productViewFragment = new ProductViewFragment();

        Bundle kk = new Bundle();
        kk.putString("categoryType", categoryType);
        productViewFragment.setArguments(kk);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, productViewFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}