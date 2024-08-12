package lk.zeamac.app.powermart.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lk.zeamac.app.powermart.R;

public class WelComePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PowerMart_FullScreen);
        setContentView(R.layout.activity_wel_come_page);

        Button wel_login = findViewById(R.id.wel_login);
        Button wel_sign_up = findViewById(R.id.wel_signup);

        wel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelComePageActivity.this, LoginActivity.class );
                startActivity(intent);

            }
        });

        wel_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelComePageActivity.this, SingupActivity.class );
                startActivity(intent);
            }
        });

    }
}