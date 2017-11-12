package com.example.jang.socialappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tsengvn.typekit.TypekitContextWrapper;

public class newPicActivity extends AppCompatActivity {
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pic);
    }

    public void closeClicked(View v){
        startActivity(new Intent(newPicActivity.this, drawerActivity.class));
        finish();
    }
    public void finishClicked(View v){
        startActivity(new Intent(newPicActivity.this, drawerActivity.class));
        finish();
    }
}
