package com.rvscript.viewmodelproject.networking;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/*
A class that we can use to get an instance of our interface
and make API calls
 */

public class RepoApi {
    private static final String BASE_URL = "https://api.github.com/";
    private static RepoService repoService;
    private static Retrofit retrofit;

    public static RepoService getInstance(){
        if(repoService != null ){
            return repoService;
        }

        if (retrofit == null){
            initializeRetrofit();
        }

        repoService = retrofit.create(RepoService.class);
        return repoService;
    }

    private static void initializeRetrofit(){
        retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build();
    }

    private RepoApi(){}
}
