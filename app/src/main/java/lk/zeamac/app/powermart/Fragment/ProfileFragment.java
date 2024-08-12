package lk.zeamac.app.powermart.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import lk.zeamac.app.powermart.Activity.LoginActivity;
import lk.zeamac.app.powermart.Activity.MainActivity;
import lk.zeamac.app.powermart.Activity.OrderViewActivity;
import lk.zeamac.app.powermart.Activity.ProfileDetailsActivity;
import lk.zeamac.app.powermart.Activity.WelComePageActivity;
import lk.zeamac.app.powermart.R;


public class ProfileFragment extends Fragment {
    SharedPreferences sharedPreferences;
    GoogleSignInClient googleClient;
    GoogleSignInOptions googleOptions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);

        googleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleClient = GoogleSignIn.getClient(getActivity(), googleOptions);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fragment.findViewById(R.id.imageViewProfileInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileDetailsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        fragment.findViewById(R.id.orderimageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderViewActivity.class);
                getActivity().startActivity(intent);
            }
        });

        fragment.findViewById(R.id.logout_imageView12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("hasLoggedIn", false);
                        editor.commit();


                        startActivity(new Intent(getActivity(), WelComePageActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
            }
        });

    }
}