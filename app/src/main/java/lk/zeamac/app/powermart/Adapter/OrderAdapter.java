package lk.zeamac.app.powermart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lk.zeamac.app.powermart.Entity.OrderEntity;
import lk.zeamac.app.powermart.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Viewholder>{

    ArrayList<OrderEntity> items;

    Context context;

    public OrderAdapter(ArrayList<OrderEntity> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_order_list, parent, false);


        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.Viewholder holder, int position) {
        holder.id.setText(items.get(position).getId());
        holder.titleText.setText(items.get(position).getTitle());
        holder.price.setText(items.get(position).getPrice());
        holder.status.setText(items.get(position).getStatus());
        holder.date.setText((CharSequence) items.get(position).getDate());
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
        TextView id;
        TextView titleText;
        ImageView img;
        TextView price;
        TextView status;
        EditText date;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.order_list_title);
            img = itemView.findViewById(R.id.order_list_img);
            price = itemView.findViewById(R.id.order_list_price);
            id = itemView.findViewById(R.id.order_list_id);
            status = itemView.findViewById(R.id.order_list_status);
            date = itemView.findViewById(R.id.order_list_date);

        }
    }
}
