package lk.zeamac.app.powermart.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import lk.zeamac.app.powermart.Entity.UserEntity;
import lk.zeamac.app.powermart.R;

public class ProfileDetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private EditText nameEditText, fullNameEditText, emailEditText, contactNoEditText;
    private Spinner nameTitleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        nameEditText = findViewById(R.id.userName);
        fullNameEditText = findViewById(R.id.fullName);
        emailEditText = findViewById(R.id.emailProfile);
        contactNoEditText = findViewById(R.id.contact1);
        nameTitleSpinner = findViewById(R.id.spinnerNameTitle);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            firestore.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserEntity userEntity = document.toObject(UserEntity.class);
                        if (userEntity != null) {
                            nameTitleSpinner.setSelection(getIndex(nameTitleSpinner, userEntity.getTitle()));
                            nameEditText.setText(userEntity.getName());
                            fullNameEditText.setText(userEntity.getFullName());
                            emailEditText.setText(userEntity.getEmail());
                            contactNoEditText.setText(userEntity.getMobile());
                        }
                    } else {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            nameEditText.setText(user.getDisplayName());
                            emailEditText.setText(user.getEmail());
                        }
                    }
                }
            });
        }

        Spinner spinner = findViewById(R.id.spinnerNameTitle);
        String[] itemsMonth = {"MR", "MRS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsMonth);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        nameEditText.setEnabled(false);
        fullNameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        contactNoEditText.setEnabled(false);
        nameTitleSpinner.setEnabled(false);

        findViewById(R.id.editDetailsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEditText.setEnabled(true);
                fullNameEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                contactNoEditText.setEnabled(true);
                nameTitleSpinner.setEnabled(true);

            }
        });


        findViewById(R.id.saveDetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameEditText.getText().toString();
                String newFullName = fullNameEditText.getText().toString();
                String newEmail = emailEditText.getText().toString();
                String newContactNo = contactNoEditText.getText().toString();
                String newNameTitle = nameTitleSpinner.getSelectedItem().toString();

                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    DocumentReference userDocument = firestore.collection("Users").document(currentUser.getUid());

                    userDocument.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("name", newName);
                                updates.put("fullName", newFullName);
                                updates.put("email", newEmail);
                                updates.put("mobile", newContactNo);
                                updates.put("title", newNameTitle);

                                userDocument.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileDetailsActivity.this, "Details updated successfully", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileDetailsActivity.this, "Failed to update details", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String userId = user.getUid();
                                if (user != null) {
                                    DocumentReference documentReference = firestore.collection("Users").document(userId);
                                    UserEntity userEntity = new UserEntity(userId, newNameTitle, newName, newFullName, newEmail, "user", newContactNo);

                                    documentReference.set(userEntity).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ProfileDetailsActivity.this, "Details updated successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProfileDetailsActivity.this, "Failed to update details", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                nameEditText.setEnabled(false);
                fullNameEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                contactNoEditText.setEnabled(false);
                nameTitleSpinner.setEnabled(false);
            }
        });

        findViewById(R.id.profileBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }
}