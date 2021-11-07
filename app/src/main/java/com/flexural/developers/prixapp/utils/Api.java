package com.flexural.developers.prixapp.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @GET("airtimeList.php")
    Call<List<DatabaseList>> getData();
}
