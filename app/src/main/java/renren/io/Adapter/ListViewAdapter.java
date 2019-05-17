package renren.io.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import renren.io.login.R;
import renren.io.model.Msg;

public class ListViewAdapter extends BaseAdapter {
    private List<Msg> msgList;
    private LayoutInflater inflater;

    public ListViewAdapter (List<Msg> msgList, Context context){
        this.msgList = msgList;
        this.inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return msgList == null?0:msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为试图
        View view = inflater.inflate(R.layout.listview_item,null);
        Msg msg = (Msg) getItem(position);

        //在view试图中查找组件
        TextView tv_test01 = (TextView) view.findViewById(R.id.tv1);
        TextView tv_test02 = (TextView) view.findViewById(R.id.tv2);

        //为组件设置相应的数据
        tv_test01.setText(msg.getTest01());
        tv_test02.setText(msg.getTest02());

        return view;
    }
}
