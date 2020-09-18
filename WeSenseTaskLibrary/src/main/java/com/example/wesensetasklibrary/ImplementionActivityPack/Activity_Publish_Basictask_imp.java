package com.example.wesensetasklibrary.ImplementionActivityPack;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wesensetasklibrary.R;
import com.example.wesensetasklibrary.basicClasses.Task;
import com.example.wesensetasklibrary.interfacesPack.PostRequest_publishTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/*
 * Author: LJH
 * Time:2020.8.25
 * Descripition:
 * 基础感知任务发布Activity，在直接引用此类进行应用页面构建时，需要使用Intent传输根url和子url(功能接口)
 * 注意Intent通信使用的字符串名称为rootURLTag和funcURLTag，可在全局变量中查看
 */
public class Activity_Publish_Basictask_imp extends AppCompatActivity {
    private Task task;
    private String TAG = "Fragment_publish";
    //为日期选择设立的全局变量
    int mYear, mMonth, mDay;
    String date_tvString;
    Spinner taskKind_spinner;
    int taskKind = -1;
    private String rootURL;
    private String rootURLTag = "publish_bascitask_rootURL";
    //private String funcURL;
    //private String funcURLTag = "publish_bascitask_funcURL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_basictask_1);

        initURLString();
        initView();

    }

    private void initURLString() {
        Intent lIntent = getIntent();
        rootURL = lIntent.getStringExtra(rootURLTag);
        if (rootURL == null) {
            AlertDialog lAlertDialog = new AlertDialog.Builder(this).setTitle("Activity初始化错误").setMessage("无法连接服务器").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface pDialogInterface, int pI) {
                    pDialogInterface.dismiss();
                    finish();
                }
            }).create();
            lAlertDialog.show();
        }
    }

    private void initView() {
        findViewById(R.id.publishpage_basictaskpublish_1_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNetworkRequest();
            }
        });

        //给发布页的截止日期选择按钮添加绑定Listener
        final TextView textView = findViewById(R.id.publishpage_basictaskpublish_1_deadline_dp);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前日期
                Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);


                //创建日期选择的对话框，并绑定日期选择的Listener（都是Android内部封装的组件和方法）
                DatePickerDialog dialog = new DatePickerDialog(Activity_Publish_Basictask_imp.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        date_tvString = mYear + "." + (mMonth + 1) + "." + mDay;
                        textView.setText(date_tvString);
                    }

                }, mYear, mMonth, mDay);
                //设置最小时限
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMinDate(new Date().getTime());

                dialog.show();
            }
        });

        taskKind_spinner = findViewById(R.id.publishpage_basictaskpublish_1_taskKind_spinner);
        taskKind_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskKind = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                taskKind = 0;
            }
        });
    }

    private void postNetworkRequest() {
        final Context context = this;
        //收集当前页输入的信息
        String coins_str = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_Coins_ev)).getText().toString();
        String taskName = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_taskName_ev)).getText().toString();
        String taskMount_str = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_taskMount_ev)).getText().toString();
        String deadline = ((TextView) findViewById(R.id.publishpage_basictaskpublish_1_deadline_dp)).getText().toString();
        String describe = ((EditText) findViewById(R.id.publishpage_basictaskpublish_1_describe_ev)).getText().toString();
        if (coins_str == "" || taskName == "" || taskMount_str == "" || deadline == "" || describe == "")
            Toast.makeText(this, getString(R.string.publishTask_nullRemind), Toast.LENGTH_SHORT).show();
        else {
            float coins = Float.parseFloat(coins_str);
            int taskMount = Integer.parseInt(taskMount_str);

            int userId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", ""));
            String userName = getSharedPreferences("user", MODE_PRIVATE).getString("userName", "");
            //String timeNow = (new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss")).format(new Date(System.currentTimeMillis()));

            //建立任务Bean
            try {
                task = new Task(null, taskName, new Date(), new SimpleDateFormat("yyyy.MM.dd").parse(deadline), userId, userName, coins, describe, taskMount, 0, taskKind);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i(TAG, task.toString());

            //TODO：引用Gson类库
            Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
            String postTask = gson.toJson(task);


            //使用Retrofit类库，发送POST请求
            //TODO:引用Retrofit类库，替换BaseURL为真实有效的根URL
            Retrofit retrofit = new Retrofit.Builder().baseUrl(rootURL).addConverterFactory(GsonConverterFactory.create()).build();
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postTask);
            PostRequest_publishTask publish = retrofit.create(PostRequest_publishTask.class);
            Call<ResponseBody> call = publish.publishTask(requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(context, getResources().getString(R.string.publishSuccess), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.publishFail), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.networkconnect_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}