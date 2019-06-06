package renren.io.utils;

import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadImg extends Thread{

    public static String uploadFile(File file,String fileName){
        String imgUrl = null;
        //1.创建对应的MediaType
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        OkHttpClient client = new OkHttpClient();
        //2.创建RequestBody
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);

        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, fileBody)
                .addFormDataPart("token", (String) SharedPreferencesUtil.getData("MyDemo","token",""))
                .build();

        //4.构建请求
        Request request = new Request.Builder()
                .url("https://api.highboy.cn/renren-fast/nuohua/nhSubstitute/upload")
                .post(requestBody)
                .build();

        //5.发送请求
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            if(jsonObject.getString("code").equals("0")){
                System.out.println(jsonObject.getString("url"));
                imgUrl = jsonObject.getString("url");
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
    
}