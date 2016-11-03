package com.taozi.gifts.modules.category.util;
import com.taozi.gifts.modules.category.bean.Gift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/28.
 */
public class GiftParseUtil {
    public static List<Gift> parseGift(String json){
        List<Gift> mList=new ArrayList<Gift>();
        try {
            JSONObject jsonObject=new JSONObject(json);
            jsonObject=jsonObject.getJSONObject("data");
            JSONArray jsonArray=jsonObject.getJSONArray("nodes");
            for(int i=0;i<jsonArray.length();i++){
                Gift mGift=new Gift();
                try{
                    JSONObject jsonObj=jsonArray.getJSONObject(i);
                    mGift.setId(jsonObj.getInt("id"));
                    mGift.setTitle(jsonObj.getString("title").substring(5));
                    mGift.setImage1("http://www.songliapp.com"+jsonObj.getString("image1"));
                    mGift.setPrice(jsonObj.getString("price"));
                    mGift.setPraiseCount(jsonObj.getInt("praiseCount"));
                    mGift.setMarketPrice(jsonObj.getString("marketPrice"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                mList.add(mGift);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }
}
