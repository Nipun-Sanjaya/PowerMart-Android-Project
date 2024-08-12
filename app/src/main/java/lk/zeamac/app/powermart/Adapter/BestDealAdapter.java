package lk.zeamac.app.powermart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Activity.SingleProductViewActivity;
import lk.zeamac.app.powermart.Entity.BestDealItemsEntity;
import lk.zeamac.app.powermart.R;

public class BestDealAdapter extends RecyclerView.Adapter<BestDealAdapter.viewholder> {

    ArrayList<BestDealItemsEntity> best_items;
    Context context;

    public BestDealAdapter(ArrayList<BestDealItemsEntity> items) {
        this.best_items = items;
    }

    @NonNull
    @Override
    public BestDealAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_best_deal, parent, false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BestDealAdapter.viewholder holder, int position) {

        holder.titleText.setText(best_items.get(position).getTitle());
        holder.priceText.setText(best_items.get(position).getPrice());

        int drawableResourcesId = holder.itemView.getResources()
                .getIdentifier(best_items.get(position).getImgPath(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourcesId)
                .into(holder.pic);

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductViewActivity.class);
                context.startActivity(intent);
            }
        });

        holder.plusMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductViewActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return best_items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleText,priceText,plusMark;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleTextDeal);
            priceText = itemView.findViewById(R.id.dealPrice);
            pic = itemView.findViewById(R.id.imgBestDeal);
            plusMark = itemView.findViewById(R.id.textView13);
        }
    }
}
