package com.example.edupay.network;





import com.example.edupay.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by devanshramen on 11/19/17.
 */

public class NetworkUtils {



    public static APIService getAPIService() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        Retrofit.Builder retrofit = new Retrofit.Builder();
        client.connectTimeout(180, TimeUnit.SECONDS);
        client.readTimeout(180, TimeUnit.SECONDS);
        client.writeTimeout(180, TimeUnit.SECONDS);
        client.addInterceptor(httpLoggingInterceptor);
        retrofit.baseUrl(BuildConfig.BASE_URL_API)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
            //    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.build().create(APIService.class);
    }

}
