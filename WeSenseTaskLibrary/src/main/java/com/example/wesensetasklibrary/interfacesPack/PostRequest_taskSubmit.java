package com.example.wesensetasklibrary.interfacesPack;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostRequest_taskSubmit {
   /*
    @Multipart
    @POST("XXXX")
    Call<ResponseBody> task_Submission(@Part("subText") String subText, @Part List<MultipartBody.Part> picList);*/


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/")                                                //Post目标地址
    Call<ResponseBody> task_submit(@Body RequestBody user_task_Info);
}
