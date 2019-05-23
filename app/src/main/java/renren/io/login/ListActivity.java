package renren.io.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import renren.io.Adapter.ListViewAdapter;
import renren.io.model.Msg;
import renren.io.utils.SharedPreferencesUtil;

public class ListActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView textView;
    private Button button;

    private String list_url = "http://47.110.63.155/renren-fast/nuohua/nhSubstitute/list";
    private List<Msg> msgList = new ArrayList<>();
    private JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//即隐藏标题栏
        //getSupportActionBar().hide();// 隐藏ActionBar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//即全屏
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.tv1) ;
        recyclerView = (RecyclerView) findViewById(R.id.lv_id);
        button = (Button) findViewById(R.id.bt1);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);

        final Intent intent = new Intent();

        if(!("").equals(SharedPreferencesUtil.getData("MyDemo","username",""))){
            textView.setText((String) SharedPreferencesUtil.getData("MyDemo","username",""));
        }else {
            textView.setText("Hello！");
        }

        loadData();
        //初始状态
//        swipeRefreshLayout .post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//                loadData();
//            }
//        });

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });




        ListViewAdapter listViewAdapter = new ListViewAdapter(msgList,ListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this,LinearLayoutManager.VERTICAL,false));//横向布局
        //recyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
        //将adapter和listView邦定
        recyclerView.setAdapter(listViewAdapter);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        listViewAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
            //长按
            @Override
            public void onItemLongClick(View view, int pos) {
                PopupMenu popupMenu = new PopupMenu(ListActivity.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.meun_shezhi02,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.mu01:
                                Toast.makeText(ListActivity.this,"修改",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.mu02:
                                Toast.makeText(ListActivity.this,"删除",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }

            //单击
            @Override
            public void onItemLClick(View view, int pos) {
                intent.setClass(ListActivity.this,DetailsActivity.class);
                intent.putExtra("1",jsonObject.getJSONArray("nhSubstituteList").getJSONObject(pos).toString());
                startActivity(intent);
            }
        });


        //setOnCreateContextMenuListener与onContextItemSelected配套使用
//        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0,0,0,"修改");
//                menu.add(0,1,0,"删除");
//            }
//        });




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
                                intent.setClass(ListActivity.this,AddSubstituteActivity.class);
                                startActivity(intent);
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
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int mid = (int) info.id;
//        switch (item.getItemId()){
//            case 0:
//                Toast.makeText(ListActivity.this,"功能1",Toast.LENGTH_SHORT).show();
//                break;
//            case 1:
//                Toast.makeText(ListActivity.this,"功能1",Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return super.onContextItemSelected(item);
//    }

    public void loadData(){
        final JSONObject jsonParam = new JSONObject();
        jsonParam.put("username", SharedPreferencesUtil.getData("MyDemo","username",""));
        new Thread(){
            @Override
            public void run() {

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(list_url);
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
                        jsonObject = JSONObject.parseObject(result);
                        if(jsonObject.getString("code").equals("0")){
                            JSONArray jsonArray = jsonObject.getJSONArray("nhSubstituteList");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject subObject = jsonArray.getJSONObject(i);
                                Msg msg = new Msg();
                                msg.setTest01(subObject.getString("personnelName"));
                                System.out.println(subObject.getString("personnelName"));
                                msg.setTest02(subObject.getString("idCard"));
                                msgList.add(msg);

                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
