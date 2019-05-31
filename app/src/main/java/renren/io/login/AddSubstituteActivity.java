package renren.io.login;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import renren.io.utils.SharedPreferencesUtil;

public class AddSubstituteActivity extends Activity {
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

    private String Add_Url = "https://api.highboy.cn/renren-fast/nuohua/nhSubstitute/save";

    private static final int TAKE_PHOTO = 11;// 拍照
    private static final int CROP_PHOTO = 12;// 裁剪图片
    private static final int LOCAL_CROP = 13;// 本地图库

    private Uri imageUri;// 拍照时的图片uri


    private String mFilePath = Environment.getExternalStorageDirectory().getPath() + "/" +
            String.valueOf(System.currentTimeMillis()) + "photo.jpg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_substitute);
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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        final Intent intent = new Intent();
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(AddSubstituteActivity.this,ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

                if(TextUtils.isEmpty(personnelName)||TextUtils.isEmpty(idCard)||TextUtils.isEmpty(num)||TextUtils.isEmpty(chargeunitPrice)||TextUtils.isEmpty(basicWage)||
                        TextUtils.isEmpty(employeeId)||TextUtils.isEmpty(tranches)||TextUtils.isEmpty(sectionNumber)||TextUtils.isEmpty(orderNum)||TextUtils.isEmpty(signature)){
                    Toast.makeText(AddSubstituteActivity.this,"所以有条件不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                final JSONObject jsonParam = new JSONObject();
                jsonParam.put("personnelName",personnelName);
                jsonParam.put("idCard",idCard);
                jsonParam.put("num",num);
                jsonParam.put("chargeunitPrice",chargeunitPrice);
                jsonParam.put("basicWage",basicWage);
                jsonParam.put("employeeId",employeeId);
                jsonParam.put("tranches",tranches);
                jsonParam.put("sectionNumber",sectionNumber);
                jsonParam.put("orderNum",orderNum);
                jsonParam.put("signature",signature);
                jsonParam.put("personnelPic","1");
                jsonParam.put("payPic","2");
                jsonParam.put("paymentStatus",1);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                jsonParam.put("billingDate",df.format(new Date()));
                jsonParam.put("paymentDate",df.format(new Date()));

                new Thread(){
                    @Override
                    public void run() {
                        HttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(Add_Url);
                        StringEntity entity;

                        try {
                            entity = new StringEntity(jsonParam.toString(),"UTF-8");
                            System.out.println(jsonParam.toString());
                            entity.setContentEncoding("UTF-8");
                            entity.setContentType("application/json");
                            post.setEntity(entity);
                            System.out.println(SharedPreferencesUtil.getData("MyDemo","token",""));
                            post.setHeader("token", (String) SharedPreferencesUtil.getData("MyDemo","token",""));
                            HttpResponse response = client.execute(post);
                            System.out.println(response.getStatusLine().getStatusCode());
                            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                                String result = EntityUtils.toString(response.getEntity());
                                System.out.println(result);
                                JSONObject jsonObject = JSONObject.parseObject(result);
                                if(jsonObject.getString("code").equals("0")){
                                    Looper.prepare();
                                    Toast.makeText(AddSubstituteActivity.this,"添加成功",Toast.LENGTH_SHORT).show();

                                    intent.setClass(AddSubstituteActivity.this,ListActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Looper.loop();
                                }

                                //401没有token返回登录界面,需要登录
                            }else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                                intent.setClass(AddSubstituteActivity.this,MainActivity.class);
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
                showPopupWindow(img01);

            }
        });
        img02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(img02);

            }
        });

    }
    private void showPopupWindow(ImageView imageView){
        View popView = View.inflate(this,R.layout.popupwindow_camera_local,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn01);
        Button bt_camera = (Button) popView.findViewById(R.id.btn02);
        Button bt_cancel = (Button) popView.findViewById(R.id.btn03);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setAnimationStyle(R.style.TextAppearance_AppCompat_Button);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (ContextCompat.checkSelfPermission(AddSubstituteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddSubstituteActivity.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                popupWindow.dismiss();
                //创建File对象 用于存储拍照后的图片
                //Environment.getExternalStorageDirectory() 内部存储
                //getExternalCacheDir() 外部存储        内部存储更保险 不会因为cd卡被拔丢失  不太重要的存外部存储   灵活运用 不要纠结
                File outputImage=new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                //file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/test/" + System.currentTimeMillis() + ".jpg");

                try {
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    //创建新的File对象
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=24){
                    //第一个参数是content  第二个参数是 任意的唯一的字符串  第三个参数是File对象     FileProvider是一个特殊的内容提供者需在AndroidManifest中注册
                    imageUri=FileProvider.getUriForFile(AddSubstituteActivity.this,"renren.io.login.fileProvider",outputImage);
                }else {
                    imageUri=Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
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
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);
    }

    /**
     * 打开相册
     */
    private void openAlbum(){

        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,LOCAL_CROP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"你已经拒绝了权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    try {
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        img01.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case LOCAL_CROP:
                if (resultCode==RESULT_OK){
                    //4.4级以上系统使用这个方法处理图片
                    HandleImageOnKitKat(data);
                }
            default:
                break;
        }
    }

    private void HandleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docid=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                //解析出数字格式的id
                String id=docid.split(":")[1];
                String selection =MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri=ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docid));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是cotent类型的Uri，则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return  path;
    }
    private void displayImage(String imagePath){
        if (imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            img01.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }


}
