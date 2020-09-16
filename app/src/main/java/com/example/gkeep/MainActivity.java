package com.example.gkeep;

import androidx.appcompat.app.AppCompatActivity;

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
    private ArrayList<itemsValue> items;
    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          mItemtitle = findViewById(R.id.et_title);
          mLinearLayout = findViewById(R.id.ll_dynamic);
          mAddList = findViewById(R.id.ll_addList);
          mAdddata = findViewById(R.id.btn_adddata);
          items = new ArrayList<>();

          dBhelper = new DBhelper(MainActivity.this);
    }

    public void onAddListItem(View view){
        itemId++;
       mAddList.setEnabled(false);
       mAddList.setAlpha(0.5f);
        mAdddata.setEnabled(false);
        mAdddata.setAlpha(0.5f);

        View newView = LayoutInflater.from(MainActivity.this).inflate(R.layout.cell_insert_data,null);
        CheckBox ch = newView.findViewById(R.id.ch_insert);
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
        String itemArray = null;
        if (items.size() > 0) {
            JSONArray itemsArray = new JSONArray();
            for (itemsValue itemvalue : items) {
                try {
                    JSONObject itemsObject = new JSONObject();
                    itemsObject.put("itemID", itemvalue.itemID);
                    itemsObject.put("itemName", itemvalue.itemName);
                    itemsObject.put("isChecked", itemvalue.isChecked);
                    itemsArray.put(itemsObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            itemArray = itemsArray.toString();
        }
        dBhelper.insertDataToDatabase(dBhelper.getWritableDatabase(), itemsTitle, itemArray);
        mItemtitle.setText("");
        mLinearLayout.removeAllViews();
    }
}