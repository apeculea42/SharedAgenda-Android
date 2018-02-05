package com.myherobots.sharedagenda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mindorks.placeholderview.PlaceHolderView;
import com.myherobots.MyApplication;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "myApp";
    FirebaseUser user;

    Toolbar mToolbar;

    public static String uId;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;

    private ViewPager mViewPager;
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FacebookSdk.sdkInitialize(getApplicationContext());



        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent myIntent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(myIntent);
            finish();
        }

       mViewPager = findViewById(R.id.pager);
       mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
       setupViewPager(mViewPager);

        Context context = MyApplication.getAppContext();
        sharedPref = context.getSharedPreferences("UsersNames", Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        if (user != null) {
           uId = user.getUid();
           editor.putString(uId, user.getDisplayName());
           editor.apply();

       }
    }

    public SharedPreferences sendPref() {
        return sharedPref;
    }

    public void receivePhotoUrl(String url){
        photoUrl = url;
        sendPhotoUrl();
    }

    public void sendPhotoUrl() {


        editor.putString(uId.concat("photo"), photoUrl);
        editor.apply();
    }


    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentUser(), "FragmentUser");
        adapter.addFragment(new FragmentPartener(), "FragmentPartener");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber);
    }

    public String sendId(){
        return uId;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupDrawer() {
        Log.d(TAG, "CALLED METHOD SETUPDRAWER");
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_REQUESTS));


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dot_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                // Do whatever you want to do on logout click.
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();

                /*
                Intent myIntent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(myIntent);

*/

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
