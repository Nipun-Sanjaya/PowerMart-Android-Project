package lk.zeamac.app.powermart.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Activity.MainActivity;
import lk.zeamac.app.powermart.Activity.SingleProductViewActivity;
import lk.zeamac.app.powermart.Adapter.CartAdapter;
import lk.zeamac.app.powermart.Adapter.CategoryViewAdapter;
import lk.zeamac.app.powermart.Adapter.ProductViewAdapter;
import lk.zeamac.app.powermart.Entity.CartEntity;
import lk.zeamac.app.powermart.Entity.CategoryEntity;
import lk.zeamac.app.powermart.Entity.ProductViewEntity;
import lk.zeamac.app.powermart.R;


public class ProductViewFragment extends Fragment implements CategoryViewAdapter.OnCategoryClickListener,ProductViewAdapter.OnProductClickListener {

    private ProductViewAdapter productViewAdapter;
    private CategoryViewAdapter categoryAdapter;
    private RecyclerView recyclerViewCategory, recyclerViewProduct;

    private SearchView searchView;

    private ArrayList<ProductViewEntity> productItems;

    private ArrayList<CategoryEntity> categoryItems;
    private FirebaseFirestore fireStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();

        searchView = fragment.findViewById(R.id.searchView1);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        fragment.findViewById(R.id.backbtnshop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

//Category
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
//        recyclerViewCategory = fragment.findViewById(R.id.category_product_view);
//        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        categoryAdapter = new CategoryViewAdapter(items, this::OnCategoryClick);
//        recyclerViewCategory.setAdapter(categoryAdapter);

        categoryItems = new ArrayList<>();
        RecyclerView itemView = fragment.findViewById(R.id.category_product_view);
        CategoryViewAdapter categoryAdapter = new CategoryViewAdapter(categoryItems,getActivity(),this::OnCategoryClick);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);

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
                            CategoryEntity old = categoryItems.stream().filter(i -> i .getId()
                                            .equals(category.getId()))
                                    .findFirst()
                                    .orElse(null);


                            if(old != null){
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
//AllProductView
//        item = new ArrayList<>();
//        item.add(new ProductViewEntity("power_tools", "Power Tools", "1000", "Power Tools"));
//        item.add(new ProductViewEntity("grainder", "Grainder", "1000", "Power Tools"));
//        item.add(new ProductViewEntity("agricultural_equipment", "agricultural", "1000", "Agricultural Equipment"));
//        item.add(new ProductViewEntity("grainder", "Grainder", "1000", "Power Tools"));
//        item.add(new ProductViewEntity("power_tools", "Power Tools", "1000", "Power Tools"));
//        item.add(new ProductViewEntity("machinery_post", "Genarator", "1000", "Machinery"));
//        item.add(new ProductViewEntity("power_tools", "Power Tools", "1000", "Power Tools"));
//        item.add(new ProductViewEntity("grainder", "Drill", "1000", "Power Tools"));
//        item.add(new ProductViewEntity("power_tools", "Power Tools", "1000", "Power Tools"));
//
//
//        recyclerViewProduct = fragment.findViewById(R.id.View_product_RecyclerView);
//        recyclerViewProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        productViewAdapter = new ProductViewAdapter(item);
//        recyclerViewProduct.setAdapter(productViewAdapter);
        //     View Product
        productItems = new ArrayList<>();
        RecyclerView itemView1 = fragment.findViewById(R.id.View_product_RecyclerView);
        productViewAdapter = new ProductViewAdapter(productItems, getActivity(),this::OnProductClick);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        itemView1.setLayoutManager(layoutManager);
        itemView1.setAdapter(productViewAdapter);

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

                        productViewAdapter.notifyDataSetChanged();

                        Bundle kk = getArguments();
                        if(kk != null){
                            String categoryType = kk.getString("categoryType");
                            filterProductInCategory(categoryType);
                        }
                    }
                });

        //     View Product

    }
    @Override
    public void OnCategoryClick(String categoryType, String categoryName) {
        ArrayList<ProductViewEntity> filteredList = new ArrayList<>();
        for (ProductViewEntity product : productItems) {
            if (product.getCategory().equals(categoryType)) {
                filteredList.add(product);
            }
        }

        productViewAdapter.setFilterSearchList(filteredList);
    }



    private void filterList(String text) {
        ArrayList<ProductViewEntity> itemsList = new ArrayList<>();
        for (ProductViewEntity product : productItems) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                itemsList.add(product);
            }
        }
        if (itemsList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            productViewAdapter.setFilterSearchList(itemsList);
        }

    }

    private void filterProductInCategory(String categoryType){
        ArrayList<ProductViewEntity> filteredList = new ArrayList<>();
        for (ProductViewEntity product : productItems) {
            if (product.getCategory().equals(categoryType)) {
                filteredList.add(product);
            }
        }

        productViewAdapter.setFilterSearchList(filteredList);
    }

    @Override
    public void OnProductClick(String product) {
        Intent intent = new Intent(getActivity(), SingleProductViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("productId", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
