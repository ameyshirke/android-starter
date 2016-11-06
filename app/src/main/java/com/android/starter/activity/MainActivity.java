package com.android.starter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.starter.R;
import com.android.starter.app.Application;
import com.android.starter.model.HttpBin;
import com.android.starter.rest.util.RestCall;
import com.android.starter.rest.util.RestCallbackImpl;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        final TextView view = (TextView) findViewById(R.id.tv_result);
        view.setMovementMethod(new ScrollingMovementMethod());

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RestCall<HttpBin> call = Application.getTestService().getIp();
                call.enqueue(new RestCallbackImpl<HttpBin>(call, MainActivity.this) {

                    @Override
                    public void success(Response<HttpBin> response) {

                        final HttpBin httpBin = response.body();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                System.out.println(httpBin.getOrigin());
                                view.setText("Result: " + httpBin.getOrigin());
                            }
                        });
                    }
                });
            }
        });


    }
}
