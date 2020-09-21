package com.example.gkeep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity implements taskListAdapter.taskItemClickListener {

    private DBhelper dBhelper;
    private RecyclerView mRctasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        dBhelper = new DBhelper(ViewActivity.this);
        mRctasks = (RecyclerView) findViewById(R.id.rc_tasklist);
       // mRctasks.setLayoutManager(new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL));
        loadDataToDatabase();
    }
    public void onAddTask(View view){
        startActivityForResult(new Intent(ViewActivity.this,MainActivity.class),1000);
    }
    private void loadDataToDatabase(){
        mRctasks.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        ArrayList<taskDetail> taskDetails = dBhelper.getDataFromDatabase(dBhelper.getReadableDatabase());
        taskListAdapter adapter = new taskListAdapter(ViewActivity.this,taskDetails);
        adapter.setListener(this);
        mRctasks.setAdapter(adapter);
    }



    @Override
    public void onTaskItemClicked(taskDetail taskdetail, itemsValue itemsvalue, Boolean checked) {
        ArrayList<itemsValue> ClickedItemsValue = taskdetail.convertStringToList(taskdetail.taskValue);
        for( itemsValue updatedvalue:ClickedItemsValue){
            if(updatedvalue.itemID == itemsvalue.itemID){
                updatedvalue.isChecked = checked;
                break;
            }
        }
        String updatedTaskString = taskdetail.convertItemListTOString(ClickedItemsValue);
        dBhelper.updateDataToDatabase(dBhelper.getWritableDatabase(),taskdetail.taskTitle,updatedTaskString,taskdetail.taskId);
        loadDataToDatabase();
    }

    @Override
    public void onUpdate(taskDetail taskdetail) {
        Intent updateIntent = new Intent(ViewActivity.this,MainActivity.class);
        updateIntent.putExtra("tasks",taskdetail);
      //  updateIntent.putExtra("items", (Serializable) itemsvalue);
        updateIntent.putExtra("is_update",true);
        startActivityForResult(updateIntent,1000);
    }

    @Override
    public void onDelete(taskDetail taskdetail) {
        dBhelper.deleteDataFromDatabase(dBhelper.getWritableDatabase(),taskdetail);
        loadDataToDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                loadDataToDatabase();
            }
        }
    }

}