package lk.zeamac.app.powermart.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import lk.zeamac.app.powermart.R;

public class AboutusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent("android.intent.action.DIAL",Uri.parse("tel:+94 784641491"));
                startActivity(callIntent);

            }
        });

        findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("kanipunsanjaya14@gmail.com") + "?subject=" +
                        Uri.encode("kanipunsanjaya14@gmail.com ") + "&body=" + Uri.encode("Hello world");

                Uri uri = Uri.parse(uriText);
                intent.setData(uri);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        findViewById(R.id.aboutUsFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.facebook.com");
            }

            private void gotoUrl(String s) {
                Uri uri = Uri.parse(s);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        findViewById(R.id.youtube)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoUrl("https://www.youtube.com/");


                    }


                    private void gotoUrl(String s) {
                        Uri uri = Uri.parse(s);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                });
        findViewById(R.id.backAboutUsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutusActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}