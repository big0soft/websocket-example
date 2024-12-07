package com.big0soft.websocket.echo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static final String ANDROID_EMULATOR_LOCALHOST = "192.168.1.3";
    public static final String SERVER_PORT = "9901";

    private static RestClient instance;
    private static final Object lock = new Object();

    public static RestClient getInstance() {
        RestClient instance = RestClient.instance;
        if (instance == null) {
            synchronized (lock) {
                instance = RestClient.instance;
                if (instance == null) {
                    RestClient.instance = instance = new RestClient();
                }
            }
        }
        return instance;
    }

    private final ExampleRepository mExampleRepository;

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + ANDROID_EMULATOR_LOCALHOST + ":" + SERVER_PORT + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mExampleRepository = retrofit.create(ExampleRepository.class);
    }

    public ExampleRepository getExampleRepository() {
        return mExampleRepository;
    }
}