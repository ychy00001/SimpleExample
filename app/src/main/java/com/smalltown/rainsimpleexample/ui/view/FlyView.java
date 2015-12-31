package com.smalltown.rainsimpleexample.ui.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.mode.Fly;

import java.util.ArrayList;

/**
 * 自定义飞行图
 * Created by yangchunyu on 2015/12/30 16:45
 */
public class FlyView extends View {

    private int defFlysCount = 8;

    public FlyView(Context context) {
        super(context);
        initView();
    }

    public FlyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FlyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
/*************************************分割线********************************************/

    Bitmap droid;       // 所有的飞行物都用的bitmap
    int numFlys = 0;  // 当前飞行物数量
    ArrayList<Fly> flys = new ArrayList<>(); // 当前所有飞行物列表

    //用于驱动所有的飞行物动画. 而不可能是数以百计的独立动画,
    // 我们只用一个来更新所有的动画
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime; // 用于追踪动画的时间和fps
    int frames = 0;     // 用来跟踪每秒的帧数
    Paint textPaint;    // 用于显示帧数
    float fps = 0;      //每秒帧
    Matrix m = new Matrix(); //矩阵在渲染时控制飞行物的平移旋转
    String fpsString = "";
    String numFlysString = "";
    /**
     * 初始化View
     */
    private void initView() {
        droid = BitmapFactory.decodeResource(getResources(), R.drawable.droid);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);
        // 此监听器监听飞行动画. 每一帧的动画我们都会计算消失时间
        // 更新每一个飞行物的位置旋转相符的速度
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (float)(nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlys; ++i) {
                    Fly fly = flys.get(i);
                    fly.y += (fly.speed * secs);
                    if (fly.y > getHeight()) {
                        //如果飞行物到底端 则将他移动到最上面
                        fly.y = 0 - fly.height;
                    }
                    fly.rotation = fly.rotation + (fly.rotationSpeed * secs);
                }
                // 重绘飞行物 将他们显示在新的位置和角度
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
    }

    int getNumFlys() {
        return numFlys;
    }

    private void setNumFlys(int quantity) {
        numFlys = quantity;
        numFlysString = "numFlakes: " + numFlys;
    }

    /**
     * 添加明确的数量至飞行物列表
     */
   public void addFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            flys.add(Fly.createFlake(getWidth(), droid));
        }
        setNumFlys(numFlys + quantity);
    }

    /**
     * 减少显示的飞行物. 我们从后往前移除
     */
    public void subtractFlakes(int quantity) {
        if(numFlys + 1 < quantity){
            return;
        }
        for (int i = 0; i < quantity; ++i) {
            int index = numFlys - i - 1;
            flys.remove(index);
        }
        setNumFlys(numFlys - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 重新设置飞行物
        flys.clear();
        numFlys = 0;
        addFlakes(defFlysCount);
        // 取消动画的执行
        animator.cancel();
        // 帧数统计重新开始
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each flake: back-translate by half its size (this allows it to rotate around its center),
        // rotate by its current rotation, translate by its location, then draw its bitmap
        for (int i = 0; i < numFlys; ++i) {
            Fly fly = flys.get(i);
            m.setTranslate(-fly.width/2, -fly.height/2);
            m.postRotate(fly.rotation);
            m.postTranslate(fly.width/2 + fly.x, fly.height/2 + fly.y);
            canvas.drawBitmap(fly.bitmap, m, null);
        }
        // 帧数计算: 计算我们画了多少帧 一旦第二帧开始 我们就能计算出刷新的帧数
        ++frames;
        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            float secs = (float) deltaTime / 1000f;
            fps = (float) frames / secs;
            fpsString = "fps: " + fps;
            startTime = nowTime;
            frames = 0;
        }

        canvas.drawText(numFlysString, getWidth() - 200, getHeight() - 50, textPaint);
        canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80, textPaint);
    }

    public void pause() {
        // 取消动画 不让动画在后台旋转
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }


}
