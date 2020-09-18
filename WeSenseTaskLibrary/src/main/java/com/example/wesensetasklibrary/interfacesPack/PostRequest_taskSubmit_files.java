package com.example.wesensetasklibrary.interfacesPack;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostRequest_taskSubmit_files {
   /*
    @Multipart
    @POST("XXXX")
    Call<ResponseBody> task_Submission(@Part("subText") String subText, @Part List<MultipartBody.Part> picList);*/

    @Multipart
    @POST("/xxxx")                                                //Post目标地址
    Call<ResponseBody> task_submit(@Part("utask") RequestBody user_task_Info, @Part MultipartBody.Part file);
}
