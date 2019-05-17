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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import renren.io.utils.SharedPreferencesUtil;


public class MainActivity extends Activity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    private EditText et_code;
    private ImageView img_code;
    private Button bt_button;

    private String username;
    private String password;
    private String code;

    private String img_url = "https://nhapi.hzqiaoxun.com/renren-fast/captcha.jpg?uuid=";
    private String login_url="https://nhapi.hzqiaoxun.com/renren-fast/sys/login";
    //private String img_url = "http://192.168.0.195:8080/renren-fast/captcha.jpg?uuid=";
    //private String login_url="http://192.168.0.195:8080/renren-fast/sys/login";

    //获取本机序列号(闪退)
//    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//    String serialNumber = telephonyManager.getDeviceId();
    //private String serialNumber ="123";
    //Pseudo-Unique ID, 这个在任何Android手机中都有效
    private String m_szDevIDShort = "35" + //we make this look like a valid IMEI

            Build.BOARD.length()%10 +
            Build.BRAND.length()%10 +
            Build.CPU_ABI.length()%10 +
            Build.DEVICE.length()%10 +
            Build.DISPLAY.length()%10 +
            Build.HOST.length()%10 +
            Build.ID.length()%10 +
            Build.MANUFACTURER.length()%10 +
            Build.MODEL.length()%10 +
            Build.PRODUCT.length()%10 +
            Build.TAGS.length()%10 +
            Build.TYPE.length()%10 +
            Build.USER.length()%10 ; //13 digits

    //本地存token
    /*
     * Android平台给我们提供了一个SharedPreferences类，它是一个轻量级的存储类，特别适合用于保存软件配置参数。
     * 使用SharedPreferences保存数据，其背后是用xml文件存放数据，
     * 文件存放在/data/data/<package name>/shared_prefs目录下
     * */




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.Editname);
        et_password = (EditText) findViewById(R.id.Editposs);
        et_code = (EditText) findViewById(R.id.Edittext);
        img_code = (ImageView) findViewById(R.id.Imageuuid);
        bt_button = (Button) findViewById(R.id.button_login);

        //1.设置验证码图片
        initData();
        //2.设置登录按钮和验证图片的点击事件
        bt_button.setOnClickListener(this);
        img_code.setOnClickListener(this);


    }

    private void initData() {
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet(img_url+m_szDevIDShort);
//                //httpGet.addHeader("Accept","application/octet-stream");
//                try {
//                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                    if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//                        InputStream inputStream = httpResponse.getEntity().getContent();
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        img_code.setImageBitmap(bitmap);
//                        inputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        new Thread(){
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(img_url+m_szDevIDShort);
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
                            img_code.setImageBitmap(bitmap);
                        }
                    });



                    inputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.button_login:
                login();
                break;
            case R.id.Imageuuid:
                initData();
                break;
            default:
                break;
        }
    }
    private void login(){
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        code = et_code.getText().toString().trim();//toLowercase（）输出为小写

        //判断验证码，用户名和密码不为空
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(code)){
            Toast.makeText(MainActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

//        new Thread(){
//            @Override
//            public void run() {
//                URL url = null;
//                try {
//                    url = new URL(login_url);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    //请求方式，请求信息
//                    connection.setRequestMethod("POST");
//                    connection.setReadTimeout(5000);
//                    connection.setConnectTimeout(5000);
//
//                    //设置输入输出
//                    connection.setDoOutput(true);
//                    connection.setDoInput(true);
//
//                    //post方式不能缓存，需要手动设置为false
//                    connection.setUseCaches(false);
//
//                    //请求的数据
//
//                    String data = "{\"username\":\""+URLEncoder.encode(username,"UTF-8")+"\",\"password\":\""+URLEncoder.encode(password,"UTF-8")+"\",\"captcha\":\""+URLEncoder.encode(code,"UTF-8")+"\",\"uuid\":\""+URLEncoder.encode(m_szDevIDShort,"UTF-8")+"\"}";
//
//                    //头部
//                    connection.setRequestProperty("Content-Type","application/json");
//                    //connection.setRequestProperty("Accept","application/json");
//
//                    //获取输出流
//                    OutputStream out = connection.getOutputStream();
//                    out.write(data.getBytes());
//                    out.flush();
//                    System.out.println(connection.getResponseCode());
//                    //正常访问
//                    if (connection.getResponseCode()==200){
//                        //获取相应的输入流对象
//                        StringBuffer sb = new StringBuffer();
//                        InputStream is = connection.getInputStream();
//                        InputStreamReader isr = new InputStreamReader(is);
//                        BufferedReader br = new BufferedReader(isr);
//                        String s = "";
//                        while ((s=br.readLine())!=null) {
//                            sb.append(s);
//                            System.out.println("1"+sb.toString());
//                        }
//                        JSONObject jsonObject = JSONObject.parseObject(sb.toString());
//                        System.out.println("code:"+jsonObject.getString("code"));
//                        Looper.prepare();
//                        if(jsonObject.getString("code").equals("0")){
//                            //本地存token
//
//                            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//
//                            Intent it = new Intent();
//                            it.setClass(MainActivity.this, renren.io.login.ListActivity.class);
//                            startActivity(it);
//                        }else if (jsonObject.getString("code").equals("500")&&jsonObject.getString("msg").equals("验证码不正确")){
//                            Toast.makeText(MainActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
//                            et_code.setText("");
//                        }else if (jsonObject.getString("code").equals("500")&&jsonObject.getString("msg").equals("账号或密码不正确")){
//                            Toast.makeText(MainActivity.this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
//                            et_code.setText("");
//                        }
//                        Looper.loop();
//                    }else {
//                        Looper.prepare();
//                        Toast.makeText(MainActivity.this,"网络状体不佳",Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

        new Thread(){
            @Override
            public void run() {
                //1.创建一个客户端，相当于浏览器
                HttpClient client = new DefaultHttpClient();
                //2.声明一个HttpPost请求
                HttpPost post = new HttpPost(login_url);
                //3.添加post的参数（不管是请求的数据还是响应过来的数据都封装到HttpEntity中）
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username",username);
                jsonParam.put("password",password);
                jsonParam.put("captcha",code);
                jsonParam.put("uuid",m_szDevIDShort);

                StringEntity entity = null;

                try {

                    entity = new StringEntity(jsonParam.toString(),"UTF-8");
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    post.setEntity(entity);

                    //4.执行请求后得到响应对象
                    HttpResponse response = client.execute(post);

                    //5.通过响应对象得到响应行从而得到相应码
                    int statusCode = response.getStatusLine().getStatusCode();
                    System.out.println(statusCode);
                    if(statusCode == HttpStatus.SC_OK){
                        String result = EntityUtils.toString(response.getEntity());
                        System.out.println("123 "+result);
                        //6.从HttpEntity中得到相应过来的输入流（不管是请求的数据还是响应过来的数据都封装到HttpEntity中）
                        JSONObject jsonObject = JSONObject.parseObject(result.toString());
                        System.out.println("code:"+jsonObject.getString("code"));
                        Looper.prepare();
                        if(jsonObject.getString("code").equals("0")){
                            SharedPreferencesUtil.saveData("MyDemo","username",username);
                            SharedPreferencesUtil.saveData("MyDemo","token",username);
                            SharedPreferencesUtil.saveData("MyDemo","time",System.currentTimeMillis());
                            SharedPreferencesUtil.saveData("MyDemo","expire",jsonObject.getLong("expire"));


                            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

                            Intent it = new Intent();
                            it.setClass(MainActivity.this, renren.io.login.ListActivity.class);
                            startActivity(it);
                            finish();
                        }else if (jsonObject.getString("code").equals("500")&&jsonObject.getString("msg").equals("验证码不正确")){
                            Toast.makeText(MainActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                            et_code.setText("");
                        }else if (jsonObject.getString("code").equals("500")&&jsonObject.getString("msg").equals("账号或密码不正确")){
                            Toast.makeText(MainActivity.this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
                            et_code.setText("");
                        }
                        Looper.loop();

                    }else {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

