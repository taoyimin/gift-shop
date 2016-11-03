package com.taozi.gifts.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by Tao Yimin on 2016/9/11.
 * 二次采样工具类
 */
public class SecondDecodeUtil {

    /**
     * 对Resource图片进行二次采样
     *
     * @param context   上下文对象
     * @param threshold 采样的阈值,返回的Bitmap高和宽像素都将小于阈值
     * @param id        Resource图片id
     * @return 经过二次采样之后的图片
     */
    public static Bitmap getBitmap(Context context, int threshold, int id) {
        Bitmap bmp = null;
        //获得Options对象,用于配置解码参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只解码边框
        options.inJustDecodeBounds = true;
        //第一次采样,采样图片的宽和高
        BitmapFactory.decodeResource(context.getResources(), id, options);
        //取最大值作为压缩比例,加1弥补精度的损失
        int scale = Math.max(options.outHeight / threshold, options.outWidth / threshold) + 1;
        //解码整张图片
        options.inJustDecodeBounds = false;
        //设置二次采样的压缩比
        options.inSampleSize = scale;
        //设置图片的质量(ARGB_8888,ARGB_4444,RGB_565),推荐使用RGB_565
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //第二次采样,采样整张图片
        bmp = BitmapFactory.decodeResource(context.getResources(), id, options);
        return bmp;
    }

    /**
     * 对图片文件进行二次采样
     *
     * @param threshold 采样的阈值,返回的Bitmap高和宽像素都将小于阈值
     * @param pathName  图片文件的路径
     * @return 经过二次采样之后的图片
     */
    public static Bitmap getBitmap(int threshold, String pathName) {
        Bitmap bmp = null;
        //创建文件对象
        File file = new File(pathName);
        //判断文件是否存在
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, options);
            int scale = Math.max(options.outHeight / threshold, options.outWidth / threshold) + 1;
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bmp = BitmapFactory.decodeFile(pathName, options);
            return bmp;
        } else {
            return null;
        }
    }

    /**
     * 对图片的字节数组进行二次采样
     *
     * @param threshold 采样的阈值,返回的Bitmap高和宽像素都将小于阈值
     * @param bytes     图片的字节数组
     * @return 经过二次采样之后的图片
     */
    public static Bitmap getBitmap(int threshold, byte[] bytes) {
        Bitmap bmp = null;
        //判断图片字节数组是否为空
        if (bytes != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            int scale = Math.max(options.outHeight / threshold, options.outWidth / threshold) + 1;
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            return bmp;
        } else {
            return null;
        }
    }
}
