package com.android.starter.rest.util;

import java.io.IOException;
import java.util.concurrent.Executor;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Adapts a {@link retrofit2.Call} to {@link RestCall}.
 */
public class RestCallAdapter<T> implements RestCall<T> {
    private final Call call;
    private final Executor callbackExecutor;

    RestCallAdapter(Call<T> call, Executor callbackExecutor) {
        this.call = call;
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public void cancel() {
        call.cancel();
    }

    @Override
    public void enqueue(final RestCallback<T> callback) {

        call.enqueue(new retrofit2.Callback() {
            /**
             * Invoked for a received HTTP response.
             * <p/>
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * RestCall {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(retrofit2.Call call, Response response) {

                callback.onResponse(response);
                int code = response.code();
                if (code >= 200 && code < 300) {
                    callback.success(response);
                } else if (code >= 400 && code < 500) {
                    callback.clientError(response);
                } else if (code >= 500 && code < 600) {
                    callback.serverError(response);
                } else if (code == 401) {
                    callback.unauthenticated(response);
                } else {
                    callback.unexpectedError(new RuntimeException("Unexpected response " + response));
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                if (t instanceof IOException) {
                    callback.networkError(t);
                } else {
                    callback.unexpectedError(t);
                }

            }
        });
    }

    @Override
    public RestCall<T> clone() {
        return new RestCallAdapter<>(call.clone(), callbackExecutor);
    }

    @Override
    public Response<T> execute() throws IOException {
        return call.execute();
    }
}
