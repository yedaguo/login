package renren.io.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import renren.io.utils.SharedPreferencesUtil;
import renren.io.utils.StreamTools;


public class MainActivity extends Activity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    private Button bt_button;

    private String username;
    private String password;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.Editname);
        et_password = (EditText) findViewById(R.id.Editposs);
        bt_button = (Button) findViewById(R.id.button_login);

        //1.设置验证码图片

        //2.设置登录按钮和验证图片的点击事件
        bt_button.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.button_login:
                login();
                break;
            default:
                break;
        }
    }
    private void login(){
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();

        //判断验证码，用户名和密码不为空
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                String login_url = "https://api.highboy.cn/renren-fast/nuohua/user/appLogin?username=" + username + "&password=" + password+ "";
                URL url;

                try {
                    url = new URL(login_url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(10000);
                    connection.setRequestMethod("GET");

                    System.out.println(connection.getResponseCode());
                    if(connection.getResponseCode() == HttpStatus.SC_OK){
                        InputStream in =  connection.getInputStream();
                        String content = StreamTools.readString(in);
                        //System.out.println("woshi "+StreamTools.readString(in));
                        JSONObject jsonObject = JSONObject.parseObject(content);
                        System.out.println("code:"+jsonObject.getString("code"));
                        Looper.prepare();
                        if(jsonObject.getString("code").equals("0")){
                            SharedPreferencesUtil.saveData("MyDemo","username",username);
                            SharedPreferencesUtil.saveData("MyDemo","token",jsonObject.getString("token"));
                            SharedPreferencesUtil.saveData("MyDemo","time",System.currentTimeMillis());
                            SharedPreferencesUtil.saveData("MyDemo","expire",jsonObject.getLong("expire"));
                            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

                            Intent it = new Intent();
                            it.setClass(MainActivity.this, renren.io.login.ListActivity.class);
                            startActivity(it);
                            finish();

                        }else if (jsonObject.getString("code").equals("500")&&jsonObject.getString("msg").equals("账号或密码不正确")){
                            Toast.makeText(MainActivity.this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
                            et_password.setText("");
                        }
                        Looper.loop();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

