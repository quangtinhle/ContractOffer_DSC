package de.fraunhofer.isst.dawid.contractoffer_dsc.connection;


import okhttp3.OkHttpClient;

public class OkHttpConnection {
    private static OkHttpConnection instance = null;
    private OkHttpClient client;

    private OkHttpConnection() {
        client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("admin","password"))
                .build();
    }

    public OkHttpConnection getInstance() {
        if(instance == null) {
            return new OkHttpConnection();
        }
        else
            return instance;
    }


}
