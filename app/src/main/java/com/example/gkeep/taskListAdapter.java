package com.example.gkeep;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class taskListAdapter extends RecyclerView.Adapter<taskListAdapter.taskItemHolder>{

    private Context context;
    private ArrayList<taskDetail> taskDetails;
    private taskItemClickListener listener;

    public taskListAdapter(Context context, ArrayList<taskDetail> taskDetails){
        this.context = context;
        this.taskDetails = taskDetails;
    }

    public void setListener(taskItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public taskItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new taskItemHolder(LayoutInflater.from(context).inflate(R.layout.cell_task,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull taskItemHolder holder, int position) {
        final taskDetail taskdetail = taskDetails.get(position);

        holder.mTvtasktitle.setText(taskdetail.taskTitle);
        ArrayList<itemsValue> itemvalue = taskdetail.convertStringToList(taskdetail.taskValue);
        holder.mLltaskItems.removeAllViews();

        for(int i =0;i< itemvalue.size();i++){
            final itemsValue itemValue = itemvalue.get(i);
            View taskView = LayoutInflater.from(context).inflate(R.layout.cell_view_task,null);
            TextView mTvtaskItem = taskView.findViewById(R.id.tv_task_done);
            CheckBox mChtashCheck = taskView.findViewById(R.id.ch_task_done);


            mTvtaskItem.setText(itemValue.itemName);
            mChtashCheck.setChecked(itemValue.isChecked);

            if(itemValue.isChecked){
                mTvtaskItem.setPaintFlags(TextPaint.STRIKE_THRU_TEXT_FLAG);
            }

            holder.mLltaskItems.addView(taskView);
            mChtashCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(listener!=null){
                        listener.onTaskItemClicked(taskdetail,itemValue,b);
                    }
                }
            });
        }
        holder.mIcedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onUpdate(taskdetail);
                }
            }
        });
        holder.mIcdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onDelete(taskdetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskDetails.size();
    }

    class taskItemHolder extends RecyclerView.ViewHolder{

        private TextView mTvtasktitle;
        private LinearLayout mLltaskItems;
        private ImageView mIcedit;
        private ImageView mIcdelete;

        public taskItemHolder(@NonNull View itemView) {
            super(itemView);
            mTvtasktitle = itemView.findViewById(R.id.tv_task_title);
            mLltaskItems = itemView.findViewById(R.id.ll_task_view);
            mIcedit = itemView.findViewById(R.id.ic_edit);
            mIcdelete = itemView.findViewById(R.id.ic_delete);
        }
    }

    public interface taskItemClickListener {
        void  onTaskItemClicked(taskDetail taskdetail,itemsValue itemsvalue,Boolean checked);
        void  onUpdate(taskDetail taskdetail);
        void onDelete(taskDetail taskdetail);
    }
}
