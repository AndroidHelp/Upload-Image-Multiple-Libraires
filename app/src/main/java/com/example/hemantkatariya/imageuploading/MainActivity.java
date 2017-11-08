package com.example.hemantkatariya.imageuploading;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.hemantkatariya.imageuploading.via_activity.Activity_ImageAttachment;
import com.example.hemantkatariya.imageuploading.via_fragment.Activity_FragmentImageAttachment;

/**
 * Created by Hemant Katariya on 11/8/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linear_fragment;
    private LinearLayout linear_activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Select and Upload Image");
        setContentView(R.layout.activity_main);

        linear_fragment = (LinearLayout) findViewById(R.id.linear_SelectViaFragment);
        linear_activity = (LinearLayout) findViewById(R.id.linear_SelectViaActivity);

        linear_fragment.setOnClickListener(this);
        linear_activity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.linear_SelectViaActivity:
                startActivity(new Intent(MainActivity.this, Activity_ImageAttachment.class));
                break;

            case R.id.linear_SelectViaFragment:
                startActivity(new Intent(MainActivity.this, Activity_FragmentImageAttachment.class));
                break;
        }
    }
}
