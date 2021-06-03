package com.appsinventiv.mrapplianceadmin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitClient {
    @GET("api/sms.php")
    Call<Example> createTask(
            @Query("username") String username,
            @Query("password") String password,
            @Query("sender") String sender,
            @Query("mobile") String mobile,
            @Query("message") String message

    );



}
