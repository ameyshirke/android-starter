package com.android.starter.app;

import com.android.starter.rest.service.TestService;
import com.android.starter.rest.util.ErrorHandlingCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();

    private static boolean activityVisible;

    private static TestService testService;

    public static TestService getTestService() {
        return testService;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL.getValue())
                .addCallAdapterFactory(new ErrorHandlingCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        initServices(retrofit);
    }

    private void initServices(Retrofit retrofit) {
        testService = retrofit.create(TestService.class);
    }
}
