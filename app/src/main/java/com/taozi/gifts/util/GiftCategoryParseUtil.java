package com.taozi.gifts.util;

import com.taozi.gifts.bean.GiftCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/28.
 */
public class GiftCategoryParseUtil {
    public static List<GiftCategory> parseGiftCategory(String json){
        List<GiftCategory> mList=new ArrayList<GiftCategory>();
        try {
            JSONObject jsonObject=new JSONObject(json);
            jsonObject=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject.getJSONArray("catalogs");
            for(int i=0;i<jsonArray.length();i++){
                GiftCategory mGiftCategory=new GiftCategory();
                try{
                    JSONObject jsonObj=jsonArray.getJSONObject(i);
                    mGiftCategory.setId(jsonObj.getInt("id"));
                    mGiftCategory.setTitle(jsonObj.getString("title"));
                    mGiftCategory.setImage("http://www.songliapp.com"+jsonObj.getString("image"));
                    mGiftCategory.setPraiseCount(jsonObj.getInt("praiseCount"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                mList.add(mGiftCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }
}
