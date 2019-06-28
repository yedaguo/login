package renren.io.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import renren.io.login.R;
import renren.io.model.Msg;

public class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Msg> msgList;

    private int TYPE_ITEM = 0;//正常的Item
    private int TYPE_FOOT = 1;//尾部刷新

    private boolean hasMore = true;

    public ListViewAdapter (List<Msg> msgList, Context context){
        this.context = context;
        this.msgList = msgList;
    }

    //返回不同布局
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1){
            return TYPE_FOOT;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View inflate = LayoutInflater.from(context).inflate(R.layout.listview_item ,parent,false);
            ListViewAdapter.Myholder holder= new ListViewAdapter.Myholder(inflate);
            return holder;
        }else {
            View inflate = LayoutInflater.from(context).inflate(R.layout.listview_item2 ,parent,false);
            ListViewAdapter.FootHolder holder2= new ListViewAdapter.FootHolder(inflate);
            return holder2;

        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof Myholder){
            ((Myholder)holder).textView01.setText(msgList.get(position).getTest01());
            ((Myholder)holder).textView02.setText(msgList.get(position).getTest02());

            if (onItemClickListener != null){
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onItemLongClick(holder.itemView,position);
                        return false;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemLClick(holder.itemView,position);
                    }
                });
            }
        }else {
            if(hasMore){
                ((FootHolder)holder).textView.setText("正在加载更多数据...");
            }else {
                ((FootHolder)holder).textView.setText("已经没有更多数据！");
            }
        }

    }

//    @Override
//    public void onBindViewHolder(final Myholder holder, final int i) {
//        holder.textView01.setText(msgList.get(i).getTest01());
//        holder.textView02.setText(msgList.get(i).getTest02());
//
//        if (onItemClickListener != null){
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    onItemClickListener.onItemLongClick(holder.itemView,i);
//                    return false;
//                }
//            });
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onItemLClick(holder.itemView,i);
//                }
//            });
//        }
//
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return msgList.size()+1;
    }


    public class Myholder extends RecyclerView.ViewHolder{
        TextView textView01;
        TextView textView02;

        public Myholder(View itemView) {
            super(itemView);
            textView01 = itemView.findViewById(R.id.tv1);
            textView02 = itemView.findViewById(R.id.tv2);
        }
    }

    public class FootHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public FootHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_foot);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemLongClick(View view , int pos);
        void onItemLClick(View view, int pos);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    //这里清空数据，避免下拉刷新后，还显示上拉加载的数据
    public void reData(){
        msgList.clear();
    }

    //是否有更多数据
    public void hasMore(boolean hamore) {
        this.hasMore = hamore;
    }

    public void removeItem(int position){
        msgList.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新被删除数据，以及其后面的数据
    }

}
