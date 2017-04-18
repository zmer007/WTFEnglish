package com.robusoft.lgd.wtfe.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robusoft.lgd.wtfe.R;

public abstract class TitledActivity extends FragmentActivity {

    private ViewGroup mContentView;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_title);
        mContentView = (ViewGroup) findViewById(R.id.contentContainer);
        mTitleTextView = (TextView) findViewById(R.id.titleView);
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, mContentView, true);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        mTitleTextView.setText(title);
    }
}
