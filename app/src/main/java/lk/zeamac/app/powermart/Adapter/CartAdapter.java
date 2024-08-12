package lk.zeamac.app.powermart.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lk.zeamac.app.powermart.Entity.CartEntity;
import lk.zeamac.app.powermart.Entity.CategoryEntity;
import lk.zeamac.app.powermart.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {

    private static ArrayList<CartEntity> items;
    private FirebaseStorage storage;
    private static Context context;
    private static FirebaseFirestore fireStore;
    private static OnProductClickListener onProductClickRemoveListener;


    public CartAdapter(ArrayList<CartEntity> items,Context context) {

        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }
    public interface OnProductClickListener {
        void OnProductClick(int position);
    }

    public void setOnProductClickRemoveListener(OnProductClickListener clickListener){
        this.onProductClickRemoveListener = clickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);


        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
//        holder.titleText.setText(items.get(position).getTitle());
//        holder.price_1.setText(items.get(position).getPrice());
//        holder.price_2.setText(items.get(position).getTotal_price());
//        int drawableResourcesId = holder.itemView.getResources()
//                .getIdentifier(items.get(position).getImgPath(), "drawable", holder.itemView.getContext().getPackageName());
//
//        Glide.with(context)
//                .load(drawableResourcesId)
//                .into(holder.img);
        fireStore = FirebaseFirestore.getInstance();

        CartEntity item = items.get(position);


        CollectionReference cartRef = fireStore.collection("Products");
        Query query = cartRef.whereEqualTo("id", item.getId());
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            Map<String, Object> data = documentSnapshot.getData();

                            holder.name.setText(data.get("name").toString());
                            holder.availableQty.setText(data.get("qty").toString());


                            holder.allQuantity = data.get("qty").toString();

                            holder.holderQty = Long.valueOf(holder.allQuantity);


                            holder.count = item.getQty();
                            holder.fixedPrice = item.getCartProductFixedPrice();
                            holder.totalPrice = holder.count * holder.fixedPrice;
                            holder.tprice = String.valueOf(holder.totalPrice);
                            holder.changePrice.setText(holder.tprice);


                            holder.increaseQty.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (holder.holderQty > holder.count) {
                                        holder.count++;
                                        holder.updateQty.setText("" + holder.count);

                                        holder.totalPrice = holder.count * holder.fixedPrice;
                                        holder.tprice = String.valueOf(holder.totalPrice);
                                        holder.changePrice.setText(holder.tprice);

                                        holder.UpdateQty(holder.count, item.getId());




                                    } else {
                                    }
                                }
                            });

                            holder.decreaseQty.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (holder.count <= 1) {
                                        holder.count = 1L;

                                    } else {
                                        holder.count--;
                                        holder.updateQty.setText("" + holder.count);
                                        holder.totalPrice = holder.count * holder.fixedPrice;
                                        holder.tprice = String.valueOf(holder.totalPrice);
                                        holder.changePrice.setText(holder.tprice);

                                        holder.UpdateQty(holder.count, item.getId());



                                    }
                                }
                            });


                        } else {
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        holder.price.setText(item.getCartProductFixedPrice().toString());
        holder.updateQty.setText(item.getQty().toString());
        storage.getReference("product-images/" + item.getId())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .resize(200, 200)
                                .centerCrop()
                                .into(holder.image);
                    }
                });





    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView name, price, qty, updateQty, decreaseQty, increaseQty, availableQty, changePrice;
        ImageView image,removeProduct;
        Long count, holderQty, fixedPrice, totalPrice;
        String allQuantity, tprice;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_title);
            price = itemView.findViewById(R.id.textViewFixedPrice);
            image = itemView.findViewById(R.id.imageViewCart);
            changePrice = itemView.findViewById(R.id.textChangePrice);

            updateQty = itemView.findViewById(R.id.textCartQty);
            decreaseQty = itemView.findViewById(R.id.textDecreaseQty);
            increaseQty = itemView.findViewById(R.id.textIncreaseQty);
            availableQty = itemView.findViewById(R.id.textViewAvailableQty);

            removeProduct =itemView.findViewById(R.id.tCartDeleteItem);
            removeProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onProductClickRemoveListener !=null){
                        onProductClickRemoveListener.OnProductClick(getAdapterPosition());
                    }
                }
            });
        }

        private void UpdateQty(Long count, String id) {

            DocumentReference reference = fireStore.collection("Cart").document(id);
            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot productDoc = task.getResult();
                    if (productDoc.exists()) {
                        Toast.makeText(context, "Quantity Updated !", Toast.LENGTH_SHORT).show();
                        Map<String, Object> updateMap = new HashMap<>();
                        updateMap.put("qty", count);
                        reference.update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            });
        }


    }
}