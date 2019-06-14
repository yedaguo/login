package renren.io.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renren.io.utils.SharedPreferencesUtil;

public class UpdateSubstituteActivity extends Activity {

    private EditText et;
    private EditText et01;
    private EditText et02;
    private EditText et03;
    private EditText et04;
    private EditText et05;
    private EditText et06;
    private EditText et07;
    private EditText et08;
    private EditText et09;
    private EditText et10;
    private Button btn01;
    private Button btn02;
    private ImageView img01;
    private ImageView img02;

    private String personnelName;
    private String idCard;
    private String num;
    private String chargeunitPrice;
    private String basicWage;
    private String employeeId;
    private String tranches;
    private String sectionNumber;
    private String orderNum;
    private String signature;

    private String Add_Url = "http://api.highboy.cn/renren-fast/nuohua/nhSubstitute/save";
    private String Upload_Url = "https://api.highboy.cn/renren-fast/nuohua/nhSubstitute/upload";

    private static final String TAG = "UpdateSubstitute";
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;

    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private boolean hasPermission = false;
    private File imgFile;// 拍照保存的图片文件
    private boolean click = false;

    private String fileName;

    private String imgUri1;
    private String imgUri2;
    private File file;

    private String imgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_substitute);

        btn01 = (Button) findViewById(R.id.btn01);
        btn02 = (Button) findViewById(R.id.btn02);
        img01 = (ImageView) findViewById(R.id.iv01);
        img02 = (ImageView) findViewById(R.id.iv02);
        et = (EditText) findViewById(R.id.et);
        et.setInputType(InputType.TYPE_NULL);
        et01 = (EditText) findViewById(R.id.et01);
        et02 = (EditText) findViewById(R.id.et02);
        et03 = (EditText) findViewById(R.id.et03);
        et04 = (EditText) findViewById(R.id.et04);
        et05 = (EditText) findViewById(R.id.et05);
        et06 = (EditText) findViewById(R.id.et06);
        et07 = (EditText) findViewById(R.id.et07);
        et08 = (EditText) findViewById(R.id.et08);
        et09 = (EditText) findViewById(R.id.et09);
        et10 = (EditText) findViewById(R.id.et10);

        System.out.println(getIntent().getStringExtra("1"));
        final JSONObject jsonObject = JSONObject.parseObject(getIntent().getStringExtra("1"));

        final Intent intent = new Intent();
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(UpdateSubstituteActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        et01.setText(jsonObject.getString("personnelName"));
        et02.setText(jsonObject.getString("idCard"));
        et03.setText(jsonObject.getString("num"));
        et04.setText(jsonObject.getString("chargeunitPrice"));
        et05.setText(jsonObject.getString("basicWage"));
        et06.setText(jsonObject.getString("employeeId"));
        et07.setText(jsonObject.getString("tranches"));
        et08.setText(jsonObject.getString("sectionNumber"));
        et09.setText(jsonObject.getString("orderNum"));
        et10.setText(jsonObject.getString("signature"));
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


        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personnelName = et01.getText().toString().trim();
                idCard = et02.getText().toString().trim();
                num = et03.getText().toString().trim();
                chargeunitPrice = et04.getText().toString().trim();
                basicWage = et05.getText().toString().trim();
                employeeId = et06.getText().toString().trim();
                tranches = et07.getText().toString().trim();
                sectionNumber = et08.getText().toString().trim();
                orderNum = et09.getText().toString().trim();
                signature = et10.getText().toString().trim();

                if (TextUtils.isEmpty(personnelName) || TextUtils.isEmpty(idCard) || TextUtils.isEmpty(num) || TextUtils.isEmpty(chargeunitPrice) || TextUtils.isEmpty(basicWage) ||
                        TextUtils.isEmpty(employeeId) || TextUtils.isEmpty(tranches) || TextUtils.isEmpty(sectionNumber) || TextUtils.isEmpty(orderNum) || TextUtils.isEmpty(signature)) {
                    Toast.makeText(UpdateSubstituteActivity.this, "所以有条件不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println("imgUri1是"+imgUri1);
                if (imgUri1 == null || imgUri1.length() == 0){
                    imgUri1 = jsonObject.getString("personnelPic");
                }

                System.out.println("imgUri2是"+imgUri2);
                if (imgUri2 == null || imgUri2.length() == 0){
                    imgUri2 = jsonObject.getString("payPic");
                }

                final JSONObject jsonParam = new JSONObject();
                jsonParam.put("id",jsonObject.getString("id"));
                jsonParam.put("personnelName", personnelName);
                jsonParam.put("idCard", idCard);
                jsonParam.put("num", num);
                jsonParam.put("chargeunitPrice", chargeunitPrice);
                jsonParam.put("basicWage", basicWage);
                jsonParam.put("employeeId", employeeId);
                jsonParam.put("tranches", tranches);
                jsonParam.put("sectionNumber", sectionNumber);
                jsonParam.put("orderNum", orderNum);
                jsonParam.put("signature", signature);
                jsonParam.put("personnelPic", imgUri1);
                jsonParam.put("payPic", imgUri2);
                jsonParam.put("paymentStatus", 1);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                jsonParam.put("billingDate", df.format(new Date()));
                jsonParam.put("paymentDate", df.format(new Date()));

                new Thread() {
                    @Override
                    public void run() {
                        HttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(Add_Url);
                        StringEntity entity;

                        try {
                            entity = new StringEntity(jsonParam.toString(), "UTF-8");
                            System.out.println(jsonParam.toString());
                            entity.setContentEncoding("UTF-8");
                            entity.setContentType("application/json");
                            post.setEntity(entity);
                            System.out.println(SharedPreferencesUtil.getData("MyDemo", "token", ""));
                            post.setHeader("token", (String) SharedPreferencesUtil.getData("MyDemo", "token", ""));
                            HttpResponse response = client.execute(post);
                            System.out.println(response.getStatusLine().getStatusCode());
                            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                String result = EntityUtils.toString(response.getEntity());
                                System.out.println(result);
                                JSONObject jsonObject = JSONObject.parseObject(result);
                                if (jsonObject.getString("code").equals("0")) {
                                    Looper.prepare();
                                    Toast.makeText(UpdateSubstituteActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                                    intent.setClass(UpdateSubstituteActivity.this, ListActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Looper.loop();
                                }

                                //401没有token返回登录界面,需要登录
                            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                                intent.setClass(UpdateSubstituteActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }.start();

            }
        });

        img01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = true;
                showPopupWindow();

            }
        });
        img02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = false;
                showPopupWindow();

            }
        });

    }

    private void uploadFile(final File file,final String fileName){

        new Thread(){
            @Override
            public void run() {
                super.run();
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
                        .url(Upload_Url)
                        .post(requestBody)
                        .build();

                //5.发送请求
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                    if(jsonObject.getString("code").equals("0")){
                        System.out.println(jsonObject.getString("url"));
                        imgUrl = jsonObject.getString("url");
                    }else {
                        Looper.prepare();
                        Toast.makeText(UpdateSubstituteActivity.this,"上传图片失败",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }

    private void showPopupWindow() {
        View popView = View.inflate(this, R.layout.popupwindow_camera_local, null);
        Button bt_album = (Button) popView.findViewById(R.id.btn01);
        Button bt_camera = (Button) popView.findViewById(R.id.btn02);
        Button bt_cancel = (Button) popView.findViewById(R.id.btn03);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels * 1 / 3;

        final PopupWindow popupWindow = new PopupWindow(popView, weight, height);
        popupWindow.setAnimationStyle(R.style.TextAppearance_AppCompat_Button);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateSubstituteActivity.this, "拍照", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                checkPermissions();
                if (hasPermission) {
                    openGallery();
                }

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateSubstituteActivity.this, "本地文件", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                checkPermissions();
                if (hasPermission) {
                    takePhoto();
                }

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SCAN_OPEN_PHONE);
    }


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有存储和拍照权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                hasPermission = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                Toast.makeText(this, "权限授予失败！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
            }
        }
    }

    // 拍照
    private void takePhoto() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = "photo_" + time;
        // 创建一个文件夹
        String path = Environment.getExternalStorageDirectory() + "/take_photo";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 要保存的图片文件
        imgFile = new File(file, fileName + ".jpg");
        // 将file转换成uri
        // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
        imgUri = getUriForFile(this, imgFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 添加Uri读取权限
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // 或者
//        grantUriPermission("com.rain.takephotodemo", imgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 添加图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    // 图片裁剪
    private void cropPhoto(Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        // 注意一定要添加该项权限，否则会提示无法裁剪
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.putExtra("scale", true);

        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = Uri.fromFile(imgFile);
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            fileName = "photo_" + time + ".jpg";
            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo/", fileName);
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            mCutUri = Uri.fromFile(mCutFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        this.sendBroadcast(intentBc);

        startActivityForResult(intent, REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照并进行裁剪
                case REQUEST_TAKE_PHOTO:
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_TAKE_PHOTO:" + imgUri.toString());
                    cropPhoto(imgUri, true);
                    break;

                // 裁剪后设置图片
                case REQUEST_CROP:
                    if (click){
                        img01.setImageURI(mCutUri);
                        file = new File(mCutUri.getPath());
                        uploadFile(file,fileName);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        imgUri1 = imgUrl;
                        System.out.println("wosi "+imgUri1);

                    }else {
                        img02.setImageURI(mCutUri);
                        file = new File(mCutUri.getPath());
                        uploadFile(file,fileName);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        imgUri2 = imgUrl;
                    }

                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_CROP:" + mCutUri.toString());
                    break;
                // 打开图库获取图片并进行裁剪
                case SCAN_OPEN_PHONE:
                    Log.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    cropPhoto(data.getData(), false);
                    break;

                default:
                    break;
            }
        }
    }

    // 从file中获取uri
    // 7.0及以上使用的uri是contentProvider content://com.rain.takephotodemo.FileProvider/images/photo_20180824173621.jpg
    // 6.0使用的uri为file:///storage/emulated/0/take_photo/photo_20180824171132.jpg
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "renren.io.login.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}
