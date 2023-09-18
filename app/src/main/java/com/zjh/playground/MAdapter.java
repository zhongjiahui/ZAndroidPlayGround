package com.zjh.playground;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zjh.playground.databinding.ActivityMainListBinding;

import java.util.ArrayList;

public class MAdapter extends RecyclerView.Adapter<MAdapter.MViewHolder> {

    private final ArrayList<String> data;
    private OnItemClickListener listener;

    public MAdapter(ArrayList<String> data) {
        this.data = data;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_list, parent, false);
        return new MAdapter.MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.mBinding.text.setText(data.get(position));
        if (listener != null){
            holder.itemView.setOnClickListener(v -> listener.onItemClick(v, position));
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class MViewHolder extends RecyclerView.ViewHolder{

        public ActivityMainListBinding mBinding;
        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            //DataBindingUtil.bind(itemView);
            mBinding = ActivityMainListBinding.bind(itemView);
        }
    }

    interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
