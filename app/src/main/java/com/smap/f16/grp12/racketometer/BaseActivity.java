package com.smap.f16.grp12.racketometer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * BaseActivity handling navigation drawer.
 * Inspired by: http://stackoverflow.com/a/19451842/5324369
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static String EXTRA_ACTIVITY_ID
            = "com.smap.f16.grp12.racketometer.BaseActivity.EXTRA_ACTIVITY_ID";

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private void onCreateDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null) {
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Detect if an activity changes is necessary.
        Intent intent = getIntent();
        if(intent != null) {
            if (( intent.hasCategory(Intent.CATEGORY_LAUNCHER) && // first launch
                  id == R.id.nav_performance )
                ||
                ( intent.hasExtra(EXTRA_ACTIVITY_ID) && // same activity
                  intent.getExtras().getInt(EXTRA_ACTIVITY_ID) == id) ) {

                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        }

        if (id == R.id.nav_performance) {
            startActivity(MainActivity.class, id);
        } else if (id == R.id.nav_profile) {
            startActivity(ProfileActivity.class, id);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    /**
     * Create intent to start an activity.
     * @param cls The activity to start.
     */
    private void startActivity(Class cls, int id) {
        Intent intent = new Intent(BaseActivity.this, cls);
        intent.putExtra(EXTRA_ACTIVITY_ID, id);
        startActivity(intent);
        finish();
    }
}
