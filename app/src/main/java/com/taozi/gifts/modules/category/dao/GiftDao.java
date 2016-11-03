package com.taozi.gifts.modules.category.dao;

import com.taozi.gifts.i.BaseCallBack;
import com.taozi.gifts.modules.category.bean.Gift;
import com.taozi.gifts.modules.category.util.GiftParseUtil;
import com.taozi.gifts.util.HttpUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/28.
 */
public class GiftDao {
    public static void getGiftList(String cid,final BaseCallBack callBack){
        String url="http://www.songliapp.com/SgApp/client/User/JsonVirtualCatalogProductList.php";
        HashMap<String,String> params=new HashMap();
        params.put("cid",cid);
        params.put("pf","2");
        params.put("act","1");
        params.put("page","1");
        params.put("pageSize","100");
        params.put("lowPrice","0");
        params.put("heightPrice","0");
        params.put("s","");
        HttpUtil.doHttpReqeust("POST", url, params, new BaseCallBack() {
            @Override
            public void success(Object data) {
                List<Gift> mList= GiftParseUtil.parseGift(data.toString());
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
