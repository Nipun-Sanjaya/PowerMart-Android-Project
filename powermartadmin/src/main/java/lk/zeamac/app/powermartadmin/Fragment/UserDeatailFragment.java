package lk.zeamac.app.powermartadmin.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.zeamac.app.powermartadmin.Activity.MainActivity;
import lk.zeamac.app.powermartadmin.Adapter.ProductAdapter;
import lk.zeamac.app.powermartadmin.Adapter.UserListAdapter;
import lk.zeamac.app.powermartadmin.Entity.ProductEntity;
import lk.zeamac.app.powermartadmin.Entity.UserEntity;
import lk.zeamac.app.powermartadmin.R;


public class UserDeatailFragment extends Fragment {

    private UserListAdapter userListAdapter;

    private ArrayList<UserEntity> userItems;

    private FirebaseFirestore fireStore;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_deatail, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();

        //     View User Details
        userItems = new ArrayList<>();
        RecyclerView itemView = fragment.findViewById(R.id.userDetailView);
        userListAdapter = new UserListAdapter(userItems, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        itemView.setLayoutManager(linearLayoutManager);
        itemView.setAdapter(userListAdapter);

        fireStore.collection("Users")
                //                .limit(5)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange change : value.getDocumentChanges()) {
                            UserEntity user = change.getDocument().toObject(UserEntity.class);
                            switch (change.getType()) {
                                case ADDED:
                                    userItems.add(user);
                                case MODIFIED:
                                    UserEntity old = userItems.stream().filter(i -> i.getId()
                                                    .equals(user.getId()))
                                            .findFirst()
                                            .orElse(null);


                                    if (old != null) {
                                        old.setName(user.getName());
                                        old.setEmail(user.getEmail());
                                        old.setMobile(user.getMobile());
                                        old.setType(user.getType());

                                    }
                                    break;
                                case REMOVED:
                                    userItems.remove(user);
                            }
                        }

                        userListAdapter.notifyDataSetChanged();
                    }
                });


        //     View User Details




        fragment.findViewById(R.id.backimageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
    }
}