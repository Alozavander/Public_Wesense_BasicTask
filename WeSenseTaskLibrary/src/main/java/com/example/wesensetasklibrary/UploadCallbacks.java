package com.example.wesensetasklibrary;

public interface UploadCallbacks {
    //单个文件上传的内存
    void onProgressUpdate(long uploaded);

    void onError();

    void onFinish();
}
