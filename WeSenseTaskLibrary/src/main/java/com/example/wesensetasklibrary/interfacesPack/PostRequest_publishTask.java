package com.example.wesensetasklibrary.interfacesPack;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostRequest_publishTask {
    @Headers({"Content-Type: application/json","Accept: application/json"})     //添加头
    @POST("/")                                                            //Post目标地址
    Call<ResponseBody> publishTask(@Body RequestBody publishTask);
}
