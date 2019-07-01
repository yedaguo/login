package renren.io.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import renren.io.Adapter.ListViewAdapter;
import renren.io.model.Msg;
import renren.io.utils.CallBackUtil;
import renren.io.utils.OkhttpUtil;
import renren.io.utils.SharedPreferencesUtil;

public class ListActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView textView;
    private Button button;

    private String list_url = "https://api.highboy.cn/renren-fast/nuohua/nhSubstitute/list";
    private String delete_url = "https://api.highboy.cn/renren-fast/nuohua/nhSubstitute/delete";
    private List<Msg> msgList = new ArrayList<>();
    private JSONObject jsonObject;

    private ListViewAdapter listViewAdapter;

    private int reTimes = 0;//更新次数



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

        //loadData();



        //初始状态
//        swipeRefreshLayout .post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//                loadData();
//            }
//        });







//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//            }
//        });
        loadData();

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reTimes = 0;
                        //listViewAdapter.reData();
                        msgList.clear();
                        loadData();
                        listViewAdapter.hasMore(true);
                        listViewAdapter.notifyDataSetChanged();
                        //隐藏刷新效果
                        swipeRefreshLayout.setRefreshing(false);



                    }
                },2000);
            }
        });



//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }






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
                                SharedPreferencesUtil.delData("MyDemo");
                                intent.setClass(ListActivity.this, renren.io.login.MainActivity.class);
                                startActivity(intent);
                                finish();
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
        final Map<String,String> header = new HashMap<>();
        header.put("token", (String) SharedPreferencesUtil.getData("MyDemo","token",""));
        OkhttpUtil.okHttpPostJson(list_url, jsonParam.toString(), header, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(ListActivity.this,"fail",Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onResponse(String response) {
                Toast.makeText(ListActivity.this,"success",Toast.LENGTH_SHORT).show();
                System.out.println(response);
                jsonObject = JSONObject.parseObject(response);
                if(jsonObject.getString("code").equals("0")){
                    JSONObject jsonObject1 = jsonObject.getJSONObject("page");
                    JSONArray jsonArray = jsonObject1.getJSONArray("list");
                    if(jsonArray.isEmpty()||jsonArray.size() < 1){
                        Toast.makeText(ListActivity.this,"暂无代班订单",Toast.LENGTH_SHORT).show();
                    }else {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject subObject = jsonArray.getJSONObject(i);
                            Msg msg = new Msg();
                            msg.setId(subObject.getInteger("id"));
                            msg.setOrderNum(subObject.getString("orderNum"));
                            System.out.println(subObject.getString("orderNum"));
                            msg.setBillingDate(subObject.getString("billingDate"));
                            msg.setTranches(subObject.getString("tranches"));
                            msgList.add(msg);
                        }
                    }
                    final int total = jsonObject.getJSONObject("page").getInteger("totalCount");
                    System.out.println(total);
                    listViewAdapter = new ListViewAdapter(msgList,ListActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this,LinearLayoutManager.VERTICAL,false));//横向布局
                    recyclerView.setItemAnimator(new DefaultItemAnimator());//默认动画
                    //将adapter和listView邦定
                    recyclerView.setAdapter(listViewAdapter);
                    //上拉加载

                    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            //滑到底部
                            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                                //滑到底部更新的数据
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //有多余的数据
                                        if(reTimes < total/10 ){
                                            getMoreData(reTimes);
                                        }else {
                                            //没有数据了
                                            listViewAdapter.hasMore(false);
                                            listViewAdapter.notifyDataSetChanged();
                                        }

                                    }
                                },3000);

                            }else {
                                listViewAdapter.hasMore(true);
                            }

                        }
                    });

                    final Intent intent = new Intent();
                    listViewAdapter.setOnItemClickListener(new ListViewAdapter.OnItemClickListener() {
                        //长按
                        @Override
                        public void onItemLongClick(View view, final int pos) {
                            PopupMenu popupMenu = new PopupMenu(ListActivity.this,view);
                            popupMenu.getMenuInflater().inflate(R.menu.meun_shezhi02,popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()){
                                        case R.id.mu01:
                                            Toast.makeText(ListActivity.this,"修改",Toast.LENGTH_SHORT).show();
                                            intent.setClass(ListActivity.this,UpdateSubstituteActivity.class);
                                            intent.putExtra("2",msgList.get(pos).getId());
                                            startActivity(intent);
                                            break;
                                        case R.id.mu02:
                                            Toast.makeText(ListActivity.this,"删除",Toast.LENGTH_SHORT).show();
                                            final JSONArray jsonArray = new JSONArray();
                                            jsonArray.add(msgList.get(pos).getId());
                                            System.out.println(jsonArray.toString());
                                            OkhttpUtil.okHttpPostJson(delete_url, jsonArray.toString(), header, new CallBackString() {
                                                @Override
                                                public void onFailure(Call call, Exception e) {
                                                    Toast.makeText(ListActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onResponse(String response) {
                                                    jsonObject = JSONObject.parseObject(response);
                                                    if (jsonObject.getString("code").equals("0")) {
                                                        Toast.makeText(ListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
//                                            new Thread(){
//                                                @Override
//                                                public void run() {
//                                                    HttpClient client = new DefaultHttpClient();
//                                                    HttpPost post = new HttpPost(delete_url);
//                                                    StringEntity entity;
//                                                    try {
//                                                        entity = new StringEntity(jsonArray.toString(), "UTF-8");
//                                                        entity.setContentEncoding("UTF-8");
//                                                        entity.setContentType("application/json");
//                                                        post.setEntity(entity);
//                                                        System.out.println(SharedPreferencesUtil.getData("MyDemo", "token", ""));
//                                                        post.setHeader("token", (String) SharedPreferencesUtil.getData("MyDemo", "token", ""));
//                                                        HttpResponse response = client.execute(post);
//                                                        System.out.println(response.getStatusLine().getStatusCode());
//                                                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                                                            String result = EntityUtils.toString(response.getEntity());
//                                                            System.out.println(result);
//                                                            JSONObject jsonObject = JSONObject.parseObject(result);
//                                                            if (jsonObject.getString("code").equals("0")) {
//                                                                Looper.prepare();
//                                                                Toast.makeText(ListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
//                                                                Looper.loop();
//                                                            }
//
//                                                            //401没有token返回登录界面,需要登录
//                                                        } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
//                                                            intent.setClass(ListActivity.this, MainActivity.class);
//                                                            startActivity(intent);
//                                                            finish();
//
//                                                        }
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            }.start();
//                                            msgList.remove(pos);
//                                            //listViewAdapter.notifyItemRangeRemoved(pos,msgList.size());
//                                            listViewAdapter.notifyDataSetChanged();
//                                            finish();
                                            listViewAdapter.removeItem(pos);
                                            finish();
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
                            System.out.println(msgList.get(pos).getId());
                            intent.setClass(ListActivity.this,DetailsActivity.class);
                            intent.putExtra("1",msgList.get(pos).getId());
                            startActivity(intent);
                        }
                    });
                }else {
                    Intent intent = new Intent();
                    intent.setClass(ListActivity.this, renren.io.login.ListActivity.class);
                    Toast.makeText(ListActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();

                }


            }
        });
//        final JSONObject jsonParam = new JSONObject();
//        jsonParam.put("username", SharedPreferencesUtil.getData("MyDemo","username",""));
//        new Thread(){
//            @Override
//            public void run() {
//
//                HttpClient client = new DefaultHttpClient();
//                HttpPost post = new HttpPost(list_url);
//                StringEntity entity;
//                try {
//                    entity = new StringEntity(jsonParam.toString(),"UTF-8");
//                    System.out.println(jsonParam.toString());
//                    entity.setContentEncoding("UTF-8");
//                    entity.setContentType("application/json");
//                    post.setEntity(entity);
//                    System.out.println(SharedPreferencesUtil.getData("MyDemo","token",""));
//                    post.setHeader("token", (String) SharedPreferencesUtil.getData("MyDemo","token",""));
//                    HttpResponse response = client.execute(post);
//                    System.out.println(response.getStatusLine().getStatusCode());
//                    Looper.prepare();
//                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//                        String result = EntityUtils.toString(response.getEntity());
//                        System.out.println(result);
//                        jsonObject = JSONObject.parseObject(result);
//                        if(jsonObject.getString("code").equals("0")){
//                            JSONObject jsonObject1 = jsonObject.getJSONObject("page");
//                            JSONArray jsonArray = jsonObject1.getJSONArray("list");
//                            if(jsonArray.isEmpty()||jsonArray.size() < 1){
//                                Toast.makeText(ListActivity.this,"暂无代班订单",Toast.LENGTH_SHORT).show();
//                            }else {
//                                for (int i = 0; i < jsonArray.size(); i++) {
//                                    JSONObject subObject = jsonArray.getJSONObject(i);
//                                    Msg msg = new Msg();
//                                    msg.setTest01(subObject.getString("personnelName"));
//                                    System.out.println(subObject.getString("personnelName"));
//                                    msg.setTest02(subObject.getString("idCard"));
//                                    msgList.add(msg);
//                                }
//                            }
//
//                        }
//                    }
//                    Looper.loop();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
   }

   public void getMoreData(int count){
        reTimes++;
       final JSONObject jsonParam = new JSONObject();
       jsonParam.put("username", SharedPreferencesUtil.getData("MyDemo","username",""));
       jsonParam.put("page",count+2);
       final Map<String,String> header = new HashMap<>();
       header.put("token", (String) SharedPreferencesUtil.getData("MyDemo","token",""));
       OkhttpUtil.okHttpPostJson(list_url, jsonParam.toString(), header, new CallBackUtil.CallBackString() {
           @Override
           public void onFailure(Call call, Exception e) {
               Toast.makeText(ListActivity.this,"加载失败！",Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onResponse(String response) {
               Toast.makeText(ListActivity.this,"success",Toast.LENGTH_SHORT).show();
               System.out.println(response);
               jsonObject = JSONObject.parseObject(response);
               if(jsonObject.getString("code").equals("0")) {
                   JSONObject jsonObject1 = jsonObject.getJSONObject("page");
                   JSONArray jsonArray = jsonObject1.getJSONArray("list");
                   List<Msg> msgList1 = new ArrayList<>();
                   for (int i = 0; i < jsonArray.size(); i++) {
                       JSONObject subObject = jsonArray.getJSONObject(i);
                       Msg msg = new Msg();
                       msg.setId(subObject.getInteger("id"));
                       msg.setOrderNum(subObject.getString("orderNum"));
                       System.out.println(subObject.getString("orderNum"));
                       msg.setBillingDate(subObject.getString("billingDate"));
                       msg.setTranches(subObject.getString("tranches"));
                       msgList1.add(msg);
                   }
                   msgList.addAll(msgList1);
                   listViewAdapter.hasMore(true);
                   listViewAdapter.notifyDataSetChanged();
                   System.out.println("list:"+msgList);

               }
           }
       });

   }

}
