package com.android.starter.app;

import com.android.starter.rest.service.TestService;
import com.android.starter.rest.util.ErrorHandlingCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application extends android.app.Application {

    private static TestService testService;

    public static TestService getTestService() {
        return testService;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL.getValue())
                .addCallAdapterFactory(new ErrorHandlingCallAdapterFactory())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        initServices(retrofit);
    }

    private void initServices(Retrofit retrofit) {
        testService = retrofit.create(TestService.class);
    }
}
