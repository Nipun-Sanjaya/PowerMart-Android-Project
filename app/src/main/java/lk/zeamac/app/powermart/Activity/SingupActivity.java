package lk.zeamac.app.powermart.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


import java.util.UUID;

import lk.zeamac.app.powermart.Entity.UserEntity;
import lk.zeamac.app.powermart.R;

public class SingupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore fireStore;

    private FirebaseStorage storage;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PowerMart_FullScreen);
        setContentView(R.layout.activity_singup);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        EditText nameEditText = findViewById(R.id.username);
        EditText mobileEditText = findViewById(R.id.contact_number);
        EditText emailEditText = findViewById(R.id.emailTextUser);
        EditText passwordEditText = findViewById(R.id.editTextPassword);

        //        if (firebaseAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
//            finish();
//        }

        findViewById(R.id.singup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                String id = UUID.randomUUID().toString();

                if (name.isEmpty()) {
                    Toast.makeText(SingupActivity.this, "Please Add Your UserName", Toast.LENGTH_LONG).show();
                } else if (mobile.isEmpty()) {
                    Toast.makeText(SingupActivity.this, "Please Add Your Contact Number", Toast.LENGTH_LONG).show();
                }else if (mobile.length() !=10) {
                    Toast.makeText(SingupActivity.this, "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                }else if (email.isEmpty()) {
                    Toast.makeText(SingupActivity.this, "Please Add Your Email", Toast.LENGTH_LONG).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SingupActivity.this, "Please Add Correct Email", Toast.LENGTH_LONG).show();
                }else if (password.isEmpty()) {

                    Toast.makeText(SingupActivity.this, "Please Add Your Password", Toast.LENGTH_LONG).show();
                } else {


                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.sendEmailVerification();
                                userId = firebaseAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = fireStore.collection("Users")
                                        .document(userId);
                                UserEntity userEntity = new UserEntity(id,null,name,null,email,"user",mobile);
                                documentReference.set(userEntity).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(SingupActivity.this, "Registration Successful,Please Verify Your Email", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SingupActivity.this,LoginActivity.class));
                                    }
                                });


                            } else {
                                Toast.makeText(SingupActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });


        EditText password = findViewById(R.id.editTextPassword);
        CheckBox showPassword = findViewById(R.id.checkBox1);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        TextView all_ready_exit = findViewById(R.id.all_ready_user_text);
        all_ready_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}