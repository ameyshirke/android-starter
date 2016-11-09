package com.android.starter.rest.util;

import android.app.Activity;
//import android.app.AlertDialog;
import android.support.v7.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import com.android.starter.R;
import org.json.JSONObject;
import okhttp3.MediaType;
import retrofit2.Response;

public abstract class RestCallbackImpl<T> implements RestCallback<T> {

    private static final int TOTAL_RETRIES = 3;
    private final RestCall<T> call;
    private int retryCount = 0;
    private Activity activity;

    public RestCallbackImpl(RestCall<T> call) {
        this.call = call;
    }

    public RestCallbackImpl(RestCall<T> call, Activity activity) {
        this.call = call;
        this.activity = activity;
    }

    @Override
    public void networkError(Throwable t) {

        if (retryCount++ < TOTAL_RETRIES) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    retry();
                }
            }, 10000);
        } else {
            serverError(null);
        }
    }

    @Override
    public void onResponse(Response<T> response) {
    }

    @Override
    public void serverError(Response<?> response) {

        showDialog("Server Error!");
    }

    public void clientError(Response<?> response) {
        try {
            if (response.errorBody() != null && isContentTypeJson(response)) {

                JSONObject jObject = new JSONObject(response.errorBody().string());
                if (jObject.has("message")) {

                    String error = jObject.getString("message");
                    showDialog(error);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isContentTypeJson(Response response) {

        if (response.errorBody().contentType() != null) {
            if (response.errorBody().contentType().toString().equalsIgnoreCase(MediaType.parse("application/json;charset=UTF-8").toString())) {
                return true;
            }
        }
        return false;
    }

    private void showDialog(final String msg) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {

            public void run() {
                if (activity != null && !activity.isFinishing()) {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.server_error)
                            .setMessage(msg)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });
    }

    @Override
    public void unexpectedError(Throwable t) {
        showDialog("Unexpected Error!");
    }

    @Override
    public void unauthenticated(Response<?> response) {
    }

    private void retry() {
        call.clone().enqueue(this);
    }

}
