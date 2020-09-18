package com.example.wesensetasklibrary.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.wesensetasklibrary.R;
import com.example.wesensetasklibrary.basicClasses.User_Task;
import com.example.wesensetasklibrary.interfacesPack.PostRequest_taskSubmit;
import com.example.wesensetasklibrary.interfacesPack.PostRequest_taskSubmit_files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Activity_Task_Submit extends AppCompatActivity {
    /*
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final String TAG = "Activity_Task_Submit";
    public static final String dbPath = "data/data/com.hills.mcs_02/cache" + File.separator + "sensorData" + File.separator + "sensorData.db";

    private Scroller mScroller;


    private int maxImgCount = 8;               //允许选择图片最大数
    private ArrayList<File> audioList;         //音频列表
    private Adapter_RecyclerView_TaskSubmit_Audio audio_Adapter;

    private ArrayList<File> videoList;         //视频列表
    private Adapter_RecyclerView_TaskSubmit_Video video_Adapter;

    private RecyclerView mRecyclerView;
    private Adapter_RecyclerView_TaskSubmit_Image image_Adapter;

    private NumberProgressBar mNumberProgressBar;
    private ArrayList<File> imageList;         //视频列表
    private long uploaded_now;            //目前传输总过程中已上传的数据量

    private EditText editText;
    private SQLiteDatabase SQLdb;
    private SensorManager sensorManager;
    //For test to get sensors data;
    private TextView sensorDataShow_tv;
    private BroadcastReceiver receiver;
    private Integer taskID;
    private long totalLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);

        editText = (EditText) findViewById(R.id.activity_taskSub_et);
        mScroller = new Scroller(this);
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mNumberProgressBar = findViewById(R.id.activity_taskSub_number_progress_bar);
        taskID = getIntent().getIntExtra(getResources().getString(R.string.intent_taskID_name), -1);
        if (taskID.equals(-1)) {
            Toast.makeText(this, getResources().getString(R.string.Task_Submit_requireTask_error), Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.activity_taskSub_submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.size() > 0 || videoList.size() > 0 || audioList.size() > 0)
                    uploadWithFile();
                else postRequest_Submit();
            }
        });

        initImagePicker();
        initAudioPicker();
        initVideoPicker();
        //For sensor data
        sensorDataShow_tv = (TextView) findViewById(R.id.activity_taskSub_sensorData_tv);
        //initSensorDataPicker();
    }

    //根据Intent传过来的String判定任务需要哪些传感器
    //TODO:
    private void initSensorDataPicker() {
        //String sensorNeed = getIntent().getAction();
        Button data_add = findViewById(R.id.activity_taskSub_chooseData_btn);
        data_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initImagePicker() {
        imageList = new ArrayList<File>();
        RecyclerView recyclerView = findViewById(R.id.activity_taskSub_image_rv);
        image_Adapter = new Adapter_RecyclerView_TaskSubmit_Image(Activity_Task_Submit.this, imageList);
        @SuppressLint("WrongConstant") GridLayoutManager manager = new GridLayoutManager(Activity_Task_Submit.this, 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(image_Adapter);

        Button image_add = findViewById(R.id.activity_taskSub_image_add);
        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> names = new ArrayList<>();
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                //进入相册并选择
                                PictureSelector.create(Activity_Task_Submit.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .isCamera(false)// 是否显示拍照按钮 true or false
                                        .forResult(PictureConfig.CHOOSE_REQUEST);
                        }
                    }
                }, names);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initAudioPicker() {
        audioList = new ArrayList<File>();
        //初始化Audio的RV
        RecyclerView audio_rv = findViewById(R.id.activity_taskSub_audio_rv);
        audio_rv.setLayoutManager(new LinearLayoutManager(Activity_Task_Submit.this, LinearLayoutManager.VERTICAL, false));
        audio_Adapter = new Adapter_RecyclerView_TaskSubmit_Audio(Activity_Task_Submit.this, audioList);
        audio_rv.setAdapter(audio_Adapter);

        //初始化add的按钮
        Button audio_add = findViewById(R.id.activity_taskSub_audio_add);
        audio_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RequestCodes.Audio_Search_RC);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void initVideoPicker() {
        videoList = new ArrayList<File>();
        //初始化Audio的RV
        RecyclerView video_rv = findViewById(R.id.activity_taskSub_video_rv);
        video_rv.setLayoutManager(new LinearLayoutManager(Activity_Task_Submit.this, LinearLayoutManager.VERTICAL, false));
        video_Adapter = new Adapter_RecyclerView_TaskSubmit_Video(Activity_Task_Submit.this, videoList);
        video_rv.setAdapter(video_Adapter);

        //初始化add的按钮
        Button video_add = findViewById(R.id.activity_taskSub_video_add);
        video_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, RequestCodes.Video_Search_RC);
            }
        });
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    private void initBackBT() {
        ImageView back_im = findViewById(R.id.activity_taskSub_backarrow);
        back_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void uploadWithFile() {
        //让进度条可见
        mNumberProgressBar.setVisibility(View.VISIBLE);
        //将所有图片和视频list集成
        //List<File> fileAll_list = new ArrayList<File>();
        //所有file的大小
        //totalLength = 0;

        if (imageList.size() > 0) {
            for (File file : imageList) {
                uploaded_now = 0;
                //实现上传进度监听，
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "image/*", new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        //设置进度条实时进度
                        uploaded_now += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploaded_now / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                //通过已经复写的能够监视进度的requestBody构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    User_Task user_task = new User_Task(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null, 1);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(user_task);
                    //创建Retrofit实例
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    //创建网络接口实例
                    PostRequest_taskSubmit_files subRequest = retrofit.create(PostRequest_taskSubmit_files.class);
                    //创建RequestBody
                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.task_submit(contentBody, body);
                    Log.i(TAG, postContent);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

            }
        }

        if (audioList.size() > 0) {
            for (File file : audioList) {
                uploaded_now = 0;
                //实现上传进度监听，
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "audio/*", new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        //设置进度条实时进度
                        uploaded_now += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploaded_now / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                //通过已经复写的能够监视进度的requestBody构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    User_Task user_task = new User_Task(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null, 2);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(user_task);
                    //创建Retrofit实例
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    //创建网络接口实例
                    PostRequest_taskSubmit_files subRequest = retrofit.create(PostRequest_taskSubmit_files.class);
                    //创建RequestBody
                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.task_submit(contentBody, body);
                    Log.i(TAG, postContent);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        }

        if (videoList.size() > 0) {
            for (File file : videoList) {

                uploaded_now = 0;
                //实现上传进度监听，
                ProgressRequestBody requestBody = new ProgressRequestBody(file, "video/*", new UploadCallbacks() {
                    @Override
                    public void onProgressUpdate(long uploaded) {
                        //设置进度条实时进度
                        uploaded_now += uploaded;
                        mNumberProgressBar.setProgress((int) (100 * uploaded_now / file.length()));
                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                //通过已经复写的能够监视进度的requestBody构建MultipartBody类，完成网络上传操作后续就与普通的Retrofit操作类似
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


                String subText = editText.getText().toString();

                if (subText.equals("") || subText == null) {
                    Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
                    mNumberProgressBar.setVisibility(View.GONE);
                } else {
                    User_Task user_task = new User_Task(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null, 3);
                    Gson gson = new Gson();
                    String postContent = gson.toJson(user_task);
                    //创建Retrofit实例
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    //创建网络接口实例
                    PostRequest_taskSubmit_files subRequest = retrofit.create(PostRequest_taskSubmit_files.class);
                    //创建RequestBody
                    RequestBody contentBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
                    Call<ResponseBody> call = subRequest.task_submit(contentBody, body);
                    Log.i(TAG, postContent);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(Activity_Task_Submit.this, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                                mNumberProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        }

        //最后上传文本框内容
        postRequest_Submit();
    }

    private void registBroadcastReceiver() {
        //SDU = Sense Data Update
        IntentFilter intentFilter = new IntentFilter("SDU");
        Context context = this;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String text = "";
                for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
                    String sensorName = sensor.getName();
                    String data = "";
                    String sql = "select sensorData from table_senseData where sensorName = '" + sensorName + "'ORDER BY recordTime DESC";
                    Cursor cursor = SQLdb.rawQuery(sql, null);
                    if (cursor.moveToFirst()) {
                        data = cursor.getString(0);
                    }
                    cursor.close();
                    text += sensorName + ":  " + data + "\n";
                }
                //Toast.makeText(context,"接受到SDU广播，当前Text：\n" + text,Toast.LENGTH_SHORT).show();
                Log.e(TAG, "接受到SDU广播，当前Text：\n" + text);
                sensorDataShow_tv.setText(text);
            }
        };
        registerReceiver(receiver, intentFilter);
        Log.e(TAG, "注册了广播接收器");
        //Toast.makeText(this,"注册了广播接收器",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Image Search & Add
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            List<File> fileList = new ArrayList<File>();
            if (selectList.size() > 0) {
                for (LocalMedia media : selectList) {
                    File temFile = new File(media.getPath());
                    //限制20MB大小
                    if(temFile.length()/1024 <= 20480) fileList.add(temFile);
                    else Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);
                }
                image_Adapter.AddItemList(fileList);
            } else
                Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }
        //Audio Search & Add
        if (requestCode == RequestCodes.Audio_Search_RC && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String audio_path = getPath(this, uri);
            if (audio_path != null) {
                File audio = new File(audio_path);
                //通知Audio的Adapter更新
                if(audio.length()/1024 <= 20480)  audio_Adapter.AddFooterItem(audio);
                else Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);

            } else
                Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }
        //Video Search & Add
        if (requestCode == RequestCodes.Video_Search_RC && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String video_path = getPath(this, uri);
            if (video_path != null) {
                File video = new File(video_path);
                if(video.length()/1024 <= 20480) video_Adapter.AddFooterItem(video);
                else Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_size_error), Toast.LENGTH_SHORT);
                //通知Audio的Adapter更新
            } else
                Toast.makeText(Activity_Task_Submit.this, getString(R.string.Task_Submit_add_error), Toast.LENGTH_SHORT);
        }

    }


    private void postRequest_Submit() {
        String subText = editText.getText().toString();
        int num_for_random = 0;
        final Context context = this;

        if (subText.equals("") || subText == null) {
            Toast.makeText(this, getResources().getString(R.string.Task_Submit_input_remind), Toast.LENGTH_LONG).show();
        } else {
            User_Task user_task = new User_Task(null, Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "")), taskID, 1, subText, null,0);
            Gson gson = new Gson();
            String postContent = gson.toJson(user_task);
            //创建Retrofit实例
            Retrofit retrofit = new Retrofit.Builder().baseUrl("BaseURL").addConverterFactory(GsonConverterFactory.create()).build();
            //创建网络接口实例
            PostRequest_taskSubmit subRequest = retrofit.create(PostRequest_taskSubmit.class);
            //创建RequestBody
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postContent);
            Call<ResponseBody> call = subRequest.task_submit(requestBody);
            Log.i(TAG, postContent);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(context, getResources().getString(R.string.Task_Submit_success_remind), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.Task_Submit_fail_remind), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }


    //以下皆为Uri转换path的功能代码
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    //Uri转换path的功能代码模块结束


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
