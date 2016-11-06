package com.android.starter.rest.util;

import retrofit2.Response;

public interface RestCallback<T> {

    /**
     * Called for every response.
     */
    void onResponse(Response<T> response);

    /**
     * Called for [200, 300] responses.
     */
    void success(Response<T> response);

    /**
     * Called for 401 responses.
     */
    void unauthenticated(Response<?> response);

    /**
     * Called for [400, 500] responses, except 401.
     */
    void clientError(Response<?> response);

    /**
     * Called for [500, 600] response.
     */
    void serverError(Response<?> response);

    /**
     * Called for network errors while making the call.
     */
    void networkError(Throwable t);

    /**
     * Called for unexpected errors while making the call.
     */
    void unexpectedError(Throwable t);
}
