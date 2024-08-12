package lk.zeamac.app.powermartadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.zeamac.app.powermartadmin.Entity.UserEntity;
import lk.zeamac.app.powermartadmin.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.Viewholder>{
    ArrayList<UserEntity> items;

    Context context;

    public UserListAdapter(ArrayList<UserEntity> items, FragmentActivity activity) {
        this.items = items;
    }

    @NonNull
    @Override
    public UserListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_userdeatails, parent, false);


        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.Viewholder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.type.setText(items.get(position).getType());
        holder.email.setText(items.get(position).getEmail());
        holder.mobile.setText(items.get(position).getMobile());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView name,type,email,mobile;



        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textNameView7);
            type = itemView.findViewById(R.id.textTypeView8);
            email = itemView.findViewById(R.id.userEmail);
            mobile = itemView.findViewById(R.id.userMobile);



        }
    }
}
