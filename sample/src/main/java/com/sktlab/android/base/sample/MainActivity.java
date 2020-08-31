package com.sktlab.android.base.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sktlab.android.base.util.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.isPhoneNumberValid("1", "cn");
    }
}