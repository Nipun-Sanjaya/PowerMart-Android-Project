package lk.zeamac.app.powermart.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Entity.CartEntity;
import lk.zeamac.app.powermart.Entity.ProductViewEntity;
import lk.zeamac.app.powermart.R;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ViewHolder> {
    private static ArrayList<ProductViewEntity> items;
    private FirebaseStorage storage;
    private Context context;

    OnProductClickListener onProductClickListener;

    public ProductViewAdapter(ArrayList<ProductViewEntity> items, Context context,ProductViewAdapter.OnProductClickListener productClickListener) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        this.onProductClickListener = productClickListener;
    }

    public interface OnProductClickListener {
        void OnProductClick(String product);
    }


    public void setFilterSearchList(ArrayList<ProductViewEntity> filterList) {
        this.items = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_product_view,parent,false);
        return new ProductViewAdapter.ViewHolder(view,onProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewAdapter.ViewHolder holder, int position) {
        ProductViewEntity item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());



        storage.getReference("product-images/"+item.getImage())
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,description,price,qty,rentNow;
        ImageView image;

//        Spinner categorySpinner;


        public ViewHolder(@NonNull View itemView, final ProductViewAdapter.OnProductClickListener clickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.product_view_title);

            price = itemView.findViewById(R.id.product_view_dealPrice);

            image = itemView.findViewById(R.id.imgProductView);

            rentNow = itemView.findViewById(R.id.product_view_Rent_Price_text);
            rentNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.OnProductClick(items.get(position).getId());

                    }
                }
            });

        }
    }
}