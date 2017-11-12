package com.example.jang.socialappproject;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Jang on 2017-09-30.
 */

public class App extends Application {
    public void onCreate(){
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this,"fonts/NanumMyungZo.ttf"))
                .addBold(Typekit.createFromAsset(this,"fonts/NanumMyungZo.ttf"));
    }
}
