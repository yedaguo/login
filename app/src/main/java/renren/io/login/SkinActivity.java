package renren.io.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import renren.io.utils.SharedPreferencesUtil;

public class SkinActivity extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);


        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println("1231231313");
                    Intent intent = new Intent();
                    Looper.prepare();
                    if(("").equals(SharedPreferencesUtil.getData("MyDemo","username",""))){
                        Toast.makeText(SkinActivity.this,"你还没有登录",Toast.LENGTH_SHORT).show();
                        intent.setClass(SkinActivity.this, renren.io.login.MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Long time = (Long) SharedPreferencesUtil.getData("MyDemo","time",1L);
                        if(System.currentTimeMillis()-time > (Long) SharedPreferencesUtil.getData("MyDemo","expire",1L)*1000){
                            SharedPreferencesUtil.delData("MyDemo");
                            Toast.makeText(SkinActivity.this,"长时间未登录",Toast.LENGTH_SHORT).show();
                            intent.setClass(SkinActivity.this, renren.io.login.MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            intent.setClass(SkinActivity.this, renren.io.login.ListActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    Looper.loop();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();




    }
}
