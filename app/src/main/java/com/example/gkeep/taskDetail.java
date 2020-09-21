package com.example.gkeep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class taskDetail implements Serializable {
    public int taskId;
    public String taskTitle;
    public String taskValue;

    public static String convertItemListTOString(ArrayList<itemsValue> items) {
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
        return itemArray;
    }

    public static ArrayList<itemsValue> convertStringToList(String itemsString){
        ArrayList<itemsValue> itemsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(itemsString);
            if(jsonArray.length()>0){
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jObj = jsonArray.optJSONObject(i);
                    itemsValue item = new itemsValue();
                    item.itemID = jObj.optInt("itemID");
                    item.itemName = jObj.optString("itemName");
                    item.isChecked = jObj.optBoolean("isChecked");
                    itemsList.add(item);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return itemsList;
    }
}
