package com.example.juicekaaa.notebook.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.juicekaaa.notebook.R;
import com.example.juicekaaa.notebook.bean.WeatherBean;
import com.example.juicekaaa.notebook.mode.IndexMode;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.http.POST;

/**
 * Created by Juicekaaa on 2017/10/25.
 */

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHoler> implements View.OnClickListener {
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;
    private List<IndexMode> data;

    public IndexAdapter(List<IndexMode> data) {
        this.data = data;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_item, parent, false);
        ViewHoler viewHoler = new ViewHoler(view);
        //将创建的view 添加点击事件
        view.setOnClickListener(this);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        holder.tvTitle.setText(data.get(position).getTitle());
        holder.tvTime.setText(data.get(position).getTime());
        holder.itemView.setTag(data.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnRecyclerViewItemClickListener.onItemClick(v, (int) v.getTag());
        }

    }

    public static class ViewHoler extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvTime;

        public ViewHoler(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.index_title);
            tvTime = (TextView) itemView.findViewById(R.id.index_time);

        }
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClick(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

    }

}
