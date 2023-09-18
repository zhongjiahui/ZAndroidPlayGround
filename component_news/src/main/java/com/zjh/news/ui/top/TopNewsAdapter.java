package com.zjh.news.ui.top;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zjh.news.R;
import com.zjh.news.databinding.FragmentNewsTopListBinding;
import com.zjh.news.entity.NewsData;

import java.util.List;

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.MViewHolder> {

    private List<NewsData> data;
    private OnItemClickListener listener;

    public TopNewsAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<NewsData> data){
        this.data = data;
        notifyDataSetChanged();
    }


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_top_list, parent, false);
        return new TopNewsAdapter.MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.mBinding.title.setText(data.get(position).getTitle());
        holder.mBinding.date.setText(data.get(position).getDate());
        holder.mBinding.author.setText(data.get(position).getAuthor_name());
        if (listener != null){
            holder.itemView.setOnClickListener(v -> listener.onItemClick(v, position));
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class MViewHolder extends RecyclerView.ViewHolder{

        public FragmentNewsTopListBinding mBinding;
        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentNewsTopListBinding.bind(itemView);
        }
    }

    interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
