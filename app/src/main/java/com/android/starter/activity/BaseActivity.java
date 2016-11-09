package com.android.starter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.android.starter.app.Application;

/**
 * Created by Amey on 23-07-2015.
 * <p/>
 * Use this class to perform activity actions.
 * Every activity in app should extend this class instead of android.activity.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Application.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Application.activityPaused();
    }

    protected void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }

        }

    }


}
