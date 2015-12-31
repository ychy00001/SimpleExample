package com.smalltown.rainsimpleexample.mode;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by Diagrams on 2015/12/30 16:45
 */
public class Fly {
    public float x, y;   //x y轴
    public float rotation; //旋转角度
    public float speed; //移动速度
    public float rotationSpeed; //旋转速度
    public int width, height; //宽 高
    public Bitmap bitmap;  //图片

    //缓存的一个对象映射列表
    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    /**
     * 在给定的位置中创建一个新的飞行物. Parameters of
     * location, size, rotation, and speed are randomly determined.
     */
   public static Fly createFlake(float xRange, Bitmap originalBitmap) {
        Fly flake = new Fly();
        // 根据比例创建飞行物宽高
        flake.width = (int)(5 + (float)Math.random() * 50);
        float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
        flake.height = (int)(flake.width * hwRatio);

        // 飞行物的位置水平的处在左右宽度之间 Math.random()>=0    <1
        flake.x = (float)Math.random() * (xRange - flake.width);
        // 飞行物的位置轻微的向上偏移一段距离
        flake.y = 0 - (flake.height + (float)Math.random() * flake.height);

        // 飞行物的速度是 每秒50-150像素
        flake.speed = 50 + (float) Math.random() * 150;

        // 飞行物开始的角度是-90 到 90度, 旋转是-45 到 45度
        // 角度每秒旋转
        flake.rotation = (float) Math.random() * 180 - 90;
        flake.rotationSpeed = (float) Math.random() * 90 - 45;

        //根据当前宽度 获取缓存的bitmap 其他情况下 穿件并缓存他
        flake.bitmap = bitmapMap.get(flake.width);
        if (flake.bitmap == null) {
            flake.bitmap = Bitmap.createScaledBitmap(originalBitmap,
                    (int)flake.width, (int)flake.height, true);
            bitmapMap.put(flake.width, flake.bitmap);
        }
        return flake;
    }
}
