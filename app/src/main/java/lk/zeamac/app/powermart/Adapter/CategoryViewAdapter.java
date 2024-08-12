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

import lk.zeamac.app.powermart.Entity.CategoryEntity;
import lk.zeamac.app.powermart.R;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.Viewholder> {
    static ArrayList<CategoryEntity> items;

    Context context;

    private FirebaseStorage storage;

    OnCategoryClickListener onCategoryClickListener;

    public CategoryViewAdapter(ArrayList<CategoryEntity> item,Context context,OnCategoryClickListener onCategoryClickListener) {
        this.items = item;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
        this.storage = FirebaseStorage.getInstance();
    }

    public interface OnCategoryClickListener{
        void OnCategoryClick(String categoryType,String categoryName);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_category, parent, false);

        return new Viewholder(inflate,onCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewAdapter.Viewholder holder, int position) {
//        holder.titleText.setText(items.get(position).getTitle());
//        int drawableResourcesId = holder.itemView.getResources()
//                .getIdentifier(items.get(position).getImgPath(), "drawable", holder.itemView.getContext().getPackageName());
//
//        Glide.with(context)
//                .load(drawableResourcesId)
//                .into(holder.img);
        CategoryEntity item = items.get(position);
        holder.titleText.setText(item.getName());

        storage.getReference("category-images/"+item.getImagePath())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .resize(200, 200)
                                .centerCrop()
                                .into(holder.img);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView img;

        public Viewholder(@NonNull View itemView,final OnCategoryClickListener listener) {
            super(itemView);
            titleText = itemView.findViewById(R.id.categoryTitle);
            img = itemView.findViewById(R.id.categoryImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.OnCategoryClick(items.get(position).getName(),items.get(position).getImagePath());
                    }
                }
            });
        }
    }
}
