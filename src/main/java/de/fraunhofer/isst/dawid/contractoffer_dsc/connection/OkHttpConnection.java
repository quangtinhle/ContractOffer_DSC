package de.fraunhofer.isst.dawid.contractoffer_dsc.connection;


import okhttp3.*;

import java.io.IOException;

public class OkHttpConnection {
    private static OkHttpConnection instance = null;
    private OkHttpClient client;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private OkHttpConnection() {
        client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("admin","password"))
                .build();
    }

    public static OkHttpConnection getInstance() {
        if(instance == null) {
            return new OkHttpConnection();
        }
        else
            return instance;
    }

    public String getLocation(String url, String json) throws IOException {
        Request request = getRequest(url,json);
        System.out.println(request.toString());
        Response response = getResponse(request);
        System.out.println(response.body().toString());
        return response.header("Location");
    }

    public Request getRequest(String url, String json) {

        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }

    public Response getResponse(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        return response;
    }


}