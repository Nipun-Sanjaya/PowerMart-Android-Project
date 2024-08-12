package lk.zeamac.app.powermartadmin.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.zeamac.app.powermartadmin.Entity.ProductEntity;
import lk.zeamac.app.powermartadmin.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<ProductEntity> items;
    private FirebaseStorage storage;
    private Context context;

    private static OnProductDeleteListener deleteListener;

    public ProductAdapter(ArrayList<ProductEntity> items, Context context) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }

    public interface OnProductDeleteListener{
        void onProductDelete(int position);
    }

    public void OnProductDeleteListener(OnProductDeleteListener listener){
        this.deleteListener = listener;
    }

    public void setFilterSearchList(ArrayList<ProductEntity> filterList) {
        this.items = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_product,parent,false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductEntity item = items.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText(item.getPrice());
        holder.qty.setText(item.getQty());


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

        TextView name,description,price,qty;
        ImageView image,imageDelete;


//        Spinner categorySpinner;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productTitleAdmin);
            description = itemView.findViewById(R.id.descriptionProductAdmin);
            price = itemView.findViewById(R.id.productPriceAdmin);
            qty = itemView.findViewById(R.id.productQtyAdmin);
            image = itemView.findViewById(R.id.imgProductAll);
            imageDelete = itemView.findViewById(R.id.imageView3);
//            categorySpinner = itemView.findViewById(R.id.categorySpinner);



            //delete item
            imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteListener != null){
                        deleteListener.onProductDelete(getAdapterPosition());
                    }
                }
            });

        }
    }
}
