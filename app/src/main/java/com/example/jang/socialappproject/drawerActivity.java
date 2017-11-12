package com.example.jang.socialappproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tsengvn.typekit.TypekitContextWrapper;

import static com.example.jang.socialappproject.R.menu.drawer;

public class drawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    protected  void attachBaseContext(Context newBase){
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private String[] items = {"노래","사진"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(drawerActivity.this);
                builder.setTitle("무엇을 작성하시겠어요?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),items[which],Toast.LENGTH_LONG).show();
                        if(which == 0){
                            startActivity(new Intent(drawerActivity.this, newConActivity.class));
                            finish();
                        }
                        else if(which == 1){
                            startActivity(new Intent(drawerActivity.this, newConActivity.class));
                            finish();
                        }
                    }
                });
                AlertDialog alterdialog = builder.create();
                alterdialog.show();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final LinearLayout mycontent = (LinearLayout) findViewById(R.id.mycontent);
        mycontent.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,new keyword());
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
            finish();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "앱을 종료하려면 뒤로가기를 한번더 누르세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidxManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            final LinearLayout mycontent = (LinearLayout) findViewById(R.id.mycontent);
            mycontent.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment,new myContent());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {
            final LinearLayout mycontent = (LinearLayout) findViewById(R.id.mycontent);
            mycontent.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment,new keyword());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_slideshow) {
            final LinearLayout mycontent = (LinearLayout) findViewById(R.id.mycontent);
            mycontent.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment,new picture());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_manage) {
            final LinearLayout mycontent = (LinearLayout) findViewById(R.id.mycontent);
            mycontent.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment,new music());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(drawerActivity.this, MainActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
