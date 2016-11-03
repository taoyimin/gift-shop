package com.taozi.gifts.dao;

import com.taozi.gifts.bean.GiftCategory;
import com.taozi.gifts.i.BaseCallBack;
import com.taozi.gifts.util.GiftCategoryParseUtil;
import com.taozi.gifts.util.HttpUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/28.
 */
public class GiftCategoryDao {
    public static void getGiftCategoryList(final BaseCallBack callBack){
        String url = "http://www.songliapp.com/SgApp/client/User/JsonMainPage.php";
        HashMap<String,String> params=new HashMap();
        params.put("token","");
        params.put("pf","2");
        params.put("act","1");
        params.put("page","1");
        HttpUtil.doHttpReqeust("POST", url, params, new BaseCallBack() {
            @Override
            public void success(Object data) {
                List<GiftCategory> mList= GiftCategoryParseUtil.parseGiftCategory(data.toString());
                if(mList!=null){
                    callBack.success(mList);
                }else{
                    callBack.failed(0,"请检查网络连接");
                }
            }

            @Override
            public void failed(int errorCode, Object data) {
                callBack.failed(errorCode, data);
            }
        });
    }
}
