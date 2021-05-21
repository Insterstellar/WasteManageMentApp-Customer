package com.example.wastemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wastemanagement.history.HistoryActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;


// created by Mandy Ronald
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    TextView mynumber,mynames;
    ImageView myProfilePic;
    FirebaseUser currentUser;
    String myNumber;

   // private DatabaseReference mCustomerDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUserInfo();




        currentUser=FirebaseAuth.getInstance().getCurrentUser();
         myNumber=currentUser.getPhoneNumber();

        firebaseAuth =FirebaseAuth.getInstance();
       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        //setting the users number to the navigation drawer//
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView =navigationView.getHeaderView(0);
        mynumber=headerView.findViewById(R.id.myemail);
        mynumber.setText(myNumber);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);

    }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DashFragment()).commit();
          break;
            case R.id.nav_payment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryActivity()).commit();
                break;
            case R.id.nav_pickup:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PickFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentProfile()).commit();
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ContactFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
            case R.id.nav_signout:
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void getUserInfo() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userID);
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null) {

                        NavigationView navigationView = findViewById(R.id.nav_view);
                        View headerView = navigationView.getHeaderView(0);
                        mynames = headerView.findViewById(R.id.mynameone);
                        mynames.setText(map.get("name").toString());
                    }

                        if(map.get("profileImageUrl")!=null){
                            NavigationView mYnavigation = findViewById(R.id.nav_view);
                            View headerViewM =mYnavigation.getHeaderView(0);
                            myProfilePic=headerViewM.findViewById(R.id.imageprofile);

                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(myProfilePic);
                        }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}