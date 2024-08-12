package lk.zeamac.app.powermartadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import lk.zeamac.app.powermartadmin.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTheme(R.style.Theme_PowerMart_FullScreen);

        ImageView imageView = findViewById(R.id.splashLog);

        Picasso.get().load(R.drawable.splash_load_image)
                .resize(1200, 1200)
                .into(imageView);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splashProgressBar).setVisibility(View.VISIBLE);
            }
        }, 1000);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splashProgressBar).setVisibility(View.INVISIBLE);

//                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
//                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
//
//
//                if (hasLoggedIn) {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    finish();
//                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();

            }
        }, 5000);
    }
}
