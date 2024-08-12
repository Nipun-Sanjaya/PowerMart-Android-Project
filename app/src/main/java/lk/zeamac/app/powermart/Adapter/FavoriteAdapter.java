package lk.zeamac.app.powermart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Entity.FavoriteEntity;
import lk.zeamac.app.powermart.R;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Viewholder>{
    ArrayList<FavoriteEntity> items;

    Context context;

    public FavoriteAdapter(ArrayList<FavoriteEntity> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FavoriteAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_favourite, parent, false);


        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.Viewholder holder, int position) {
        holder.titleText.setText(items.get(position).getTitle());
        holder.price_1.setText(items.get(position).getPrice());
        int drawableResourcesId = holder.itemView.getResources()
                .getIdentifier(items.get(position).getImgPath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourcesId)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView img;

        TextView price_1;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.favorite_view_title);
            img = itemView.findViewById(R.id.imgfavouriteProductView);
            price_1 = itemView.findViewById(R.id.favorite_view_dealPrice);

        }
    }
}
