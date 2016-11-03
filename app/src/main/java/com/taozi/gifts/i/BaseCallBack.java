/*
 * File Name：BaseCallBack.java
 * Copyright：Copyright 2008-2015 CiWong.Inc. All Rights Reserved.
 * Description： BaseCallBack.java
 * Modify By：se7en
 * Modify Date：2015年1月13日
 * Modify Type：Add
 */
package com.taozi.gifts.i;

/**
 * 基础异步回调类
 * 
 * @author se7en
 * @version ciwong v.1.0 2015年1月13日
 * @since ciwong v.1.0
 */
public abstract class BaseCallBack
{
    /**
     * 请求成功
     * 
     */
    public void success()
    {
    }

    /**
     * 
     * 成功
     * 
     * @param data
     *            Object 对象
     */
    public abstract void success(Object data);

    /**
     * 进度
     * 
     * @param cur
     *            当前进度
     * @param total
     *            总大小
     * @param data
     *            消息实体
     */
    public void progress(long cur, long total, Object data)
    {

    }

    /**
     *
     * 请求成功
     *
     * @param data
     *            Object 对象数组
     */
    public void success(Object... data)
    {
    }

    /**
     * 失败
     * 
     * @param errorCode
     *            错误码
     * @param data
     *            消息实体
     */
    public abstract void failed(int errorCode, Object data);

    /**
     * 服务器失败(包含400、401、403等服务器访问错误)
     * 
     * @param data
     *            失败消息实体{@link com.android.volley.VolleyError}
     */
    public void failed(Object data)
    {
    };

}
