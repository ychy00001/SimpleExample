package com.smalltown.rainsimpleexample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.smalltown.rainsimpleexample.R;

import java.util.List;

/**
 * Created by Diagrams on 2015/12/22 17:46
 */
public class AnimatorAdapter extends RecyclerView.Adapter<AnimatorAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mDatas;

    public AnimatorAdapter(Context context, List<String> data) {
        mContext = context;
        mDatas = data;
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

        }
        ViewHelper.setScaleX(holder.tv,0.6f);
        ViewHelper.setScaleY(holder.tv, 0.6f);
        ViewPropertyAnimator.animate(holder.tv).scaleY(1f).setDuration(300).start();
        ViewPropertyAnimator.animate(holder.tv).scaleX(1f).setDuration(300).start();
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

    /**********************************点击监听**********************************/
    private OnItemClickLitener mOnItemClickLitener;
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
