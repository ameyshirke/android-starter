package com.android.starter.rest.service;

import com.android.starter.model.HttpBin;
import com.android.starter.rest.util.RestCall;
import retrofit2.http.GET;

public interface TestService {

    @GET("/ip")
    RestCall<HttpBin> getIp();
}
