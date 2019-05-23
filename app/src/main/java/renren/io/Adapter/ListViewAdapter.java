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

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.Myholder> {
    private Context context;
    private List<Msg> msgList;

    public ListViewAdapter (List<Msg> msgList, Context context){
        this.context = context;
        this.msgList = msgList;
    }


    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.listview_item ,parent,false);
        ListViewAdapter.Myholder holder= new ListViewAdapter.Myholder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Myholder holder, final int i) {
        holder.textView01.setText(msgList.get(i).getTest01());
        holder.textView02.setText(msgList.get(i).getTest02());

        if (onItemClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(holder.itemView,i);
                    return false;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemLClick(holder.itemView,i);
                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return msgList.size();
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

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemLongClick(View view , int pos);
        void onItemLClick(View view, int pos);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
