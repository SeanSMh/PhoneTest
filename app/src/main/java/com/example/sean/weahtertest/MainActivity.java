package com.example.sean.weahtertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;

import java.net.URL;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements APICallback, View.OnClickListener {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private Button query;
    private EditText phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone_number = (EditText) findViewById(R.id.phone_number);  //获取输入框控件
        query = (Button) findViewById(R.id.query_weather);
        query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.query_weather) {
            sendRequestWithOkHttp();
        }
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String phoneNumber = phone_number.getText().toString();
                   // URL url = new URL("http://apicloud.mob.com/v1/mobile/address/query?key=27bdcfa312de0&phone=phoneNumber");

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://apicloud.mob.com/v1/mobile/address/query?key=27bdcfa312de0&phone=" + phoneNumber)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(final String jsonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JsonBean jsonBean = new Gson().fromJson(jsonData, JsonBean.class);  //json是要解析的json数据

                textView1 = (TextView) findViewById(R.id.city_textview);
                textView2 = (TextView) findViewById(R.id.operator_textview);
                textView3 = (TextView) findViewById(R.id.province_textview);

                textView1.setText(jsonBean.getResult().getCity());
                textView2.setText(jsonBean.getResult().getOperator());
                textView3.setText(jsonBean.getResult().getProvince());
            }
        });
/*        textView1 = (TextView) findViewById(R.id.weather_textview);
        textView2 = (TextView) findViewById(R.id.city_textview);
        Gson gson = new Gson();
        List<JsonBean> resultList = new ArrayList<JsonBean>();
        Type type = new TypeToken<ArrayList<JsonBean>>() {}.getType();
        resultList=gson.fromJson(jsonData, type);

        for(JsonBean jsonBean:resultList){
            textView1.setText(jsonBean.);
            textView2.setText(jsonBean.getMobileNumber());
        }*/


       /* System.out.println("rst:" + jsonBean.getMsg());
        System.out.println("msg:" + jsonBean.getRetCode());
        System.out.println("AirCodition:" + jsonBean.getResult().getAirCodition());
        System.out.println("City:" + jsonBean.getResult().getCity());*/

    }

    public void onSuccess(API api, int action, Map<String, Object> result) {
        switch (action) {

        }
    }

    public void onError(API api, int action, Throwable details) {

    }

}
