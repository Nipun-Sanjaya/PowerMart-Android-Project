package lk.zeamac.app.powermart.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import lk.zeamac.app.powermart.Fragment.CartFragment;
import lk.zeamac.app.powermart.Fragment.FavoriteFragment;
import lk.zeamac.app.powermart.Fragment.ProductViewFragment;
import lk.zeamac.app.powermart.R;
import lk.zeamac.app.powermart.Fragment.HomeFragment;
import lk.zeamac.app.powermart.Fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    //public static final String TAG = MainActivity.class.getName();
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;

    TextView headerName, headerEmail;
    private NavigationView navigationView;

    SharedPreferences sharedPreferences;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);



    }

    private boolean isBottomNavigationItemSelected = false;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (!isBottomNavigationItemSelected) {
            isBottomNavigationItemSelected = true;

        if (itemId == R.id.bottomNavHome || itemId == R.id.sideNavHome) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.bottomNavHome);
        } else if (itemId == R.id.bottomNavProfile || itemId == R.id.sideNavProfile) {
            loadFragment(new ProfileFragment());
            bottomNavigationView.setSelectedItemId(R.id.bottomNavProfile);
        }else if (itemId == R.id.bottomNavCart || itemId == R.id.sideNavCart) {
            loadFragment(new CartFragment());
            bottomNavigationView.setSelectedItemId(R.id.bottomNavCart);
        }else if (itemId == R.id.bottomNavShop || itemId == R.id.sideNavShop) {
            loadFragment(new ProductViewFragment());
            bottomNavigationView.setSelectedItemId(R.id.bottomNavShop);
        }else if (itemId == R.id.bottomNavFavorite || itemId == R.id.sideNavFavorite) {
            loadFragment(new FavoriteFragment());
            bottomNavigationView.setSelectedItemId(R.id.bottomNavFavorite);
        }else if (itemId == R.id.sideNavAboutUs) {
            Intent intent = new Intent(MainActivity.this, AboutusActivity.class );
            startActivity(intent);
        }else if (itemId == R.id.sideNavOrders) {
            Intent intent = new Intent(MainActivity.this, OrderViewActivity.class );
            startActivity(intent);
        }else if (itemId == R.id.sideNavLogout) {
            gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseAuth.getInstance().signOut();

                    SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("hasLoggedIn", false);
                    editor.commit();

                    finish();
                    startActivity(new Intent(MainActivity.this, WelComePageActivity.class));
                }
            });
        } else {

        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
            isBottomNavigationItemSelected = false;
        }

        return true;


    }

    public void loadFragment(Fragment fragment){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}