package renren.io.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                showPopupWindow();

            }
        });
        img02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();

            }
        });

    }
    private void showPopupWindow(){
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
                Toast.makeText(AddSubstituteActivity.this,"拍照",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddSubstituteActivity.this,"本地文件",Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

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
}
