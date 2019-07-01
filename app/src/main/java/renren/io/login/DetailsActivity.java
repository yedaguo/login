package renren.io.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import renren.io.utils.CallBackUtil;
import renren.io.utils.OkhttpUtil;
import renren.io.utils.SharedPreferencesUtil;

public class DetailsActivity extends Activity {
    private TextView tv01;
    private TextView tv02;
    private TextView tv03;
    private TextView tv04;
    private TextView tv05;
    private TextView tv06;
    private TextView tv07;
    private TextView tv08;
    private TextView tv09;
    private TextView tv10;
    private ImageView img01;
    private ImageView img02;

    private Button btn01;
    private String info_url = "https://api.highboy.cn/renren-fast/nuohua/nhSubstitute/info/";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            img01 = (ImageView) findViewById(R.id.iv01);
            img02 = (ImageView) findViewById(R.id.iv02);
            switch (msg.what){
                case 1:
                    img01.setImageBitmap((Bitmap) msg.obj);
                    break;
                case 2:
                    img02.setImageBitmap((Bitmap) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btn01 = (Button) findViewById(R.id.btn01);
        tv01 = (TextView) findViewById(R.id.tv01);
        tv02 = (TextView) findViewById(R.id.tv02);
        tv03 = (TextView) findViewById(R.id.tv03);
        tv04 = (TextView) findViewById(R.id.tv04);
        tv05 = (TextView) findViewById(R.id.tv05);
        tv06 = (TextView) findViewById(R.id.tv06);
        tv07 = (TextView) findViewById(R.id.tv07);
        tv08 = (TextView) findViewById(R.id.tv08);
        tv09 = (TextView) findViewById(R.id.tv09);
        tv10 = (TextView) findViewById(R.id.tv10);

        final Intent intent = new Intent();
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(DetailsActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Map<String,String> Param = new HashMap<>();
        final Map<String,String> header = new HashMap<>();
        header.put("token", (String) SharedPreferencesUtil.getData("MyDemo","token",""));
        System.out.println(getIntent().getIntExtra("1",1));
        OkhttpUtil.okHttpGet(info_url + getIntent().getIntExtra("1",1), Param, header, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                System.out.println("1121"+response);
                final JSONObject jsonObject1 = JSONObject.parseObject(response);
                final JSONObject jsonObject = jsonObject1.getJSONObject("substitute");
                System.out.println(jsonObject.toString());
                tv01.setText(jsonObject.getString("personnelName"));
                tv02.setText(jsonObject.getString("idCard"));
                tv03.setText(jsonObject.getString("num"));
                tv04.setText(jsonObject.getString("chargeunitPrice"));
                tv05.setText(jsonObject.getString("basicWage"));
                tv06.setText(jsonObject.getString("employeeId"));
                tv07.setText(jsonObject.getString("tranches"));
                tv08.setText(jsonObject.getString("sectionNumber"));
                tv09.setText(jsonObject.getString("orderNum"));
                tv10.setText(jsonObject.getString("signature"));

                new Thread(){
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            url = new URL(jsonObject.getString("personnelPic"));
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(10000);


                            InputStream inputStream = connection.getInputStream();

                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            Message msg = new Message();
                            msg.obj = bitmap;
                            msg.what = 1;
                            handler.sendMessage(msg);

                            //android.view.ViewRootImpl$CalledFromWrongThreadException异常处理
                            //处理方法：利用handler机制来处理，或者如下
                            //在UI线程上运行指定的操作。如果当前线程是UI线程，则立即执行操作。如果当前线程不是UI线程，则将操作发布到UI线程的事件队列。
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            img01.setImageBitmap(bitmap);
//                        }
//                    });
                            inputStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

                new Thread(){
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            url = new URL(jsonObject.getString("payPic"));
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(10000);


                            InputStream inputStream = connection.getInputStream();

                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            Message msg = new Message();
                            msg.obj = bitmap;
                            msg.what = 2;
                            handler.sendMessage(msg);



                            //android.view.ViewRootImpl$CalledFromWrongThreadException异常处理
                            //处理方法：利用handler机制来处理，或者如下
                            //在UI线程上运行指定的操作。如果当前线程是UI线程，则立即执行操作。如果当前线程不是UI线程，则将操作发布到UI线程的事件队列。
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            img02.setImageBitmap(bitmap);
//                        }
//                    });



                            inputStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

            }
        });


    }
}
