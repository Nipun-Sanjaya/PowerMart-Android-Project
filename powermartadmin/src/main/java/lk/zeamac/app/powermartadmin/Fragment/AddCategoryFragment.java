package lk.zeamac.app.powermartadmin.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import lk.zeamac.app.powermartadmin.Activity.LoginActivity;
import lk.zeamac.app.powermartadmin.Activity.MainActivity;
import lk.zeamac.app.powermartadmin.Adapter.CategoryAdapter;
import lk.zeamac.app.powermartadmin.Entity.CategoryEntity;
import lk.zeamac.app.powermartadmin.R;


public class AddCategoryFragment extends Fragment {

    private ImageButton imageButton;
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private Uri imagePath;
    private ArrayList<CategoryEntity> categoryItems;
    EditText editTextName;

    private boolean imgSelected = false;

    CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();



        fragment.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });



//     Add Category
        imageButton = fragment.findViewById(R.id.imageCategoryButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
            }
        });


        //Add New Record
        fragment.findViewById(R.id.btnAddCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextName = fragment.findViewById(R.id.addCategoryName);

                String name = editTextName.getText().toString();
                String imageId = UUID.randomUUID().toString();

                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Add Category Name", Toast.LENGTH_LONG).show();
                } else if (!imgSelected) {
                    Toast.makeText(getActivity(), "Please Add Image", Toast.LENGTH_LONG).show();
                } else {
                    CategoryEntity category = new CategoryEntity(imageId,name,imageId);


                    ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Adding new Category");
                    dialog.setCancelable(false);
                    dialog.show();

                    fireStore.collection("Categories").add(category)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    if(imagePath != null){
                                        dialog.setMessage("Uploading image...");
                                        StorageReference reference = storage.getReference("category-images")
                                                .child(imageId);
                                        reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                dialog.dismiss();
                                                categoryAdapter.notifyDataSetChanged();
                                                editTextName.setText("");
                                                imgSelected = false;
                                                int drawableResourceId = R.drawable.baseline_category_24;
                                                imageButton.setImageResource(drawableResourceId);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double progress =(100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                                dialog.setMessage("Uploading "+(int)progress+"%");

                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    dialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                                }
                            });

                }

            }
        });



//     Add Category

        //     View Category
        categoryItems = new ArrayList<>();
        RecyclerView itemView = fragment.findViewById(R.id.allCategoryRecyclerView);
        categoryAdapter = new CategoryAdapter(categoryItems,getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

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




        //     View Category








    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imagePath = result.getData().getData();
                        imgSelected = true;
                        Picasso.get()
                                .load(imagePath)
                                .resize(200, 200)
                                .centerCrop()
                                .into(imageButton);



                    }
                }
            }
    );


}

