package renren.io.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import renren.io.Adapter.ListViewAdapter;
import renren.io.model.Msg;
import renren.io.utils.SharedPreferencesUtil;

public class ListActivity extends AppCompatActivity {
    private ListView listView;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//即隐藏标题栏
        //getSupportActionBar().hide();// 隐藏ActionBar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//即全屏
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.tv1) ;
        listView = (ListView) findViewById(R.id.lv_id);
        button = (Button) findViewById(R.id.bt1);

        if(!("").equals(SharedPreferencesUtil.getData("MyDemo","username",""))){
            textView.setText((String) SharedPreferencesUtil.getData("MyDemo","username",""));
        }else {
            textView.setText("Hello！");
        }


        List<Msg> msgList = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            Msg msg = new Msg();
            msg.setTest01("数据"+i+"号");
            msg.setTest02("测试"+i+"号");
            msgList.add(msg);
        }

        ListViewAdapter listViewAdapter = new ListViewAdapter(msgList ,this);
        //将adapter和listView邦定
        listView.setAdapter(listViewAdapter);

        //setOnCreateContextMenuListener与onContextItemSelected配套使用
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0,0,0,"修改");
                menu.add(0,1,0,"删除");
            }
        });




        //设置的点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ListActivity.this,button);
                popupMenu.getMenuInflater().inflate(R.menu.meun_shezhi,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.mu01:
                                Toast.makeText(ListActivity.this,"功能1",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.mu02:
                                Toast.makeText(ListActivity.this,"功能2",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.mu03:
                                Toast.makeText(ListActivity.this,"功能3",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    //响应菜单
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int mid = (int) info.id;
        switch (item.getItemId()){
            case 0:
                Toast.makeText(ListActivity.this,"功能1",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(ListActivity.this,"功能1",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
