package com.example.gkeep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText mItemtitle;
    private LinearLayout mLinearLayout;
    private LinearLayout mAddList;
    private Button mAdddata;
    private  int itemId;
    private  Boolean isUpdate = false;
    private ArrayList<itemsValue> items;
    private DBhelper dBhelper;
    private int updateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          mItemtitle = findViewById(R.id.et_title);
          mLinearLayout = findViewById(R.id.ll_dynamic);
          mAddList = findViewById(R.id.ll_addList);
          mAdddata = findViewById(R.id.btn_adddata);
          mAdddata = findViewById(R.id.btn_adddata);
          items = new ArrayList<>();

          dBhelper = new DBhelper(MainActivity.this);

          Bundle data = getIntent().getExtras();
          if(data != null){
                isUpdate  = data.getBoolean("is_update");
               taskDetail taskdetail = (taskDetail) data.getSerializable("tasks");
              // itemsValue itemsvalue = (itemsValue) data.getSerializable("items");
               mItemtitle.setText(taskdetail.taskTitle);
               updateID = taskdetail.taskId;
               items = taskDetail.convertStringToList(taskdetail.taskValue);

               for(final itemsValue newItems:items){
                   itemId++;
                   View newView = LayoutInflater.from(MainActivity.this).inflate(R.layout.cell_insert_data,null);
                   final EditText mNewlistitem = newView.findViewById(R.id.et_newitem);
                   final ImageView mIvdone = newView.findViewById(R.id.iv_done);
                   mNewlistitem.setText(newItems.itemName);
                    mIvdone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                         newItems.itemName = mNewlistitem.getText().toString();
                         mIvdone.setVisibility(View.GONE);
                        }
                    });

                   mLinearLayout.addView(newView);
               }
               mAdddata.setText("update");
          }
    }

    public void onAddListItem(View view){
        itemId++;
       mAddList.setEnabled(false);
       mAddList.setAlpha(0.5f);
        mAdddata.setEnabled(false);
        mAdddata.setAlpha(0.5f);

        View newView = LayoutInflater.from(MainActivity.this).inflate(R.layout.cell_insert_data,null);
        final EditText mNewlistitem = newView.findViewById(R.id.et_newitem);
        final ImageView mIvdone = newView.findViewById(R.id.iv_done);
        mIvdone.setVisibility(View.GONE);

        mNewlistitem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0){
                    mIvdone.setVisibility(View.VISIBLE);
                }else{
                    mIvdone.setVisibility(View.GONE);
                }
            }
        });
        mIvdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemsValue value = new itemsValue();
                value.itemID = itemId;
                value.itemName = mNewlistitem.getText().toString();
                items.add(value);

                mIvdone.setVisibility(View.GONE);
                mAddList.setEnabled(true);
                mAddList.setAlpha(1.0f);
                mAdddata.setEnabled(true);
                mAdddata.setAlpha(1.0f);
            }
        });
        mLinearLayout.addView(newView);
    }

    public void  adddata(View view) {
        String itemsTitle = mItemtitle.getText().toString();
        if(itemsTitle.isEmpty() || items.size()==0){
            Toast.makeText(MainActivity.this,"title or list is empty",Toast.LENGTH_LONG).show();
            return;
        }
        String itemArray = null;
        itemArray = taskDetail.convertItemListTOString(items);

        if(isUpdate){
            dBhelper.updateDataToDatabase(dBhelper.getWritableDatabase(),itemsTitle,itemArray,updateID);

        }else {
            dBhelper.insertDataToDatabase(dBhelper.getWritableDatabase(), itemsTitle, itemArray);

        }
        setResult(Activity.RESULT_OK);
        finish();
    }
}