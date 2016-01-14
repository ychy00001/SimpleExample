package com.smalltown.rainsimpleexample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.smalltown.rainsimpleexample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diagrams on 2015/12/22 17:46
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
    private ArrayList<Integer> mHeights;
    private Context mContext;
    private List<String> mDatas;

    public RecycleAdapter(Context context,List<String> data) {
        mContext = context;
        mDatas = data;
        //用于瀑布流
//        mHeights = new ArrayList<>();
//        for (int i = 0; i < mDatas.size(); i++)
//        {
//            mHeights.add( (int) (300 + Math.random() * 300));
//        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = mInflater.from(mContext).inflate(R.layout.item_recycle, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(convertView);
        return viewHolder;
    }

    /**
     * 用来设置条目的信息
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.tv.getLayoutParams();
//        lp.height = mHeights.get(position);
//        holder.tv.setLayoutParams(lp);
        holder.tv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tv = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }


    /**
     * 设置加载更多后的数据
     * @param datas 加载更多后的数据
     */
    public void setLoadMoreData(List<String> datas){
        this.mDatas = datas;
    }
}
