package de.fraunhofer.isst.dawid.contractoffer_dsc.connection;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class BasicAuthInterceptor implements Interceptor {

    private String credentials;

    public BasicAuthInterceptor(String username, String password) {
        this.credentials = Credentials.basic(username,password);
    }


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization",credentials)
                .build();
        return chain.proceed(authenticatedRequest);
    }
}
