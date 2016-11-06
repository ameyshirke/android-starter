package com.android.starter.rest.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class ErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<RestCall<?>> get(Type returnType, Annotation[] annotations,
                                        Retrofit retrofit) {
        if (getRawType(returnType) != RestCall.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "RestCall must have generic type (e.g., RestCall<ResponseBody>)");
        }
        final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        final Executor callbackExecutor = retrofit.callbackExecutor();
        return new CallAdapter<RestCall<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public <R> RestCall<R> adapt(Call<R> call) {
                return new RestCallAdapter<>(call, callbackExecutor);
            }
        };
    }
}