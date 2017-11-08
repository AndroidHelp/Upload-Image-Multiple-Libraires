package com.example.hemantkatariya.imageuploading.network;

import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hemant Katariya on 04-Apr-17.
 */

public class API_Client_Request {

    private static Retrofit retrofit = null;

    public static API_Methods apiRequestService()
    {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//        okHttpClient.addInterceptor(httpLoggingInterceptor);
        okHttpClient.connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES);
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppUtils.URL_UploadRetrofit)
                    .client(okHttpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return retrofit.create(API_Methods.class);
    }
}
