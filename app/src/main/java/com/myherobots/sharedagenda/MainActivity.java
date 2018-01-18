package com.myherobots.sharedagenda;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mindorks.placeholderview.PlaceHolderView;

public class MainActivity extends AppCompatActivity {


    FirebaseUser user;
    ViewPager mViewPager;
    Toolbar mToolbar;

    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;

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
        } else {
            //displayChatMessage();
        }

        mViewPager = findViewById(R.id.pager);

        mViewPager.setAdapter(new SamplePagerAdapter(
                getSupportFragmentManager()));

        mDrawer = findViewById(R.id.drawerLayout);
        mDrawerView = findViewById(R.id.drawerView);
        setupDrawer();

    }

    private void setupDrawer() {
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

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new SampleFragment();
            } else
                return new SampleFragmentTwo();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
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
                Intent myIntent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(myIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
