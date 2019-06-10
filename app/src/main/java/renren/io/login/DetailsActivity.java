package renren.io.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        btn01 = (Button) findViewById(R.id.btn01);
        img01 = (ImageView) findViewById(R.id.iv01);
        img02 = (ImageView) findViewById(R.id.iv02);
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

        final JSONObject jsonObject = JSONObject.parseObject(getIntent().getStringExtra("1"));
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

                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    //android.view.ViewRootImpl$CalledFromWrongThreadException异常处理
                    //处理方法：利用handler机制来处理，或者如下
                    //在UI线程上运行指定的操作。如果当前线程是UI线程，则立即执行操作。如果当前线程不是UI线程，则将操作发布到UI线程的事件队列。
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img01.setImageBitmap(bitmap);
                        }
                    });
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

                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    //android.view.ViewRootImpl$CalledFromWrongThreadException异常处理
                    //处理方法：利用handler机制来处理，或者如下
                    //在UI线程上运行指定的操作。如果当前线程是UI线程，则立即执行操作。如果当前线程不是UI线程，则将操作发布到UI线程的事件队列。
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img02.setImageBitmap(bitmap);
                        }
                    });



                    inputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
