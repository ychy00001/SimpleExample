package com.smalltown.rainsimpleexample.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 流布局 内部填充TextView可以随机分布
 * Created by Diagrams on 2015/12/30 13:45
 */
public class FlowLayout  extends ViewGroup{

    public static final String TAG = "FlowLayout";

    private int childCount;
    private double horizontalSpace = 20;//两个View横向间隔
    private ArrayList<Line> lineList ;
    private double lineSpace = 20;//行间距
    private Line line;

    public FlowLayout(Context context) {
        super(context);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        lineList = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        childCount = getChildCount();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        clearLine();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int noPaddingWidth = width - getPaddingLeft() - getPaddingRight();
        int noPaddingHeight = height - getPaddingTop() - getPaddingBottom();
        Log.i(TAG,"布局宽:"+noPaddingWidth+" 布局高:"+noPaddingHeight);
        for(int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.measure(0, 0);

            if(line.getViewList().size() == 0){
                Log.i(TAG, "添加第一条");
               addToLine(childView);
            }else if (line.getWidth() + horizontalSpace + childView.getMeasuredWidth() > noPaddingWidth) {
                Log.i(TAG,"换行添加第一条目");
                addNextLine(childView);
            }else{
                Log.i(TAG,"添加其他条目");
                line.addLineView(childView);
            }
            if(i == (childCount-1) && line.getViewList().size() > 0) {
                Log.i(TAG,"添加最后一行");
                lineList.add(line);
            }
        }
        int newHeight = getPaddingTop() + getPaddingBottom();
        for(int i = 0; i < lineList.size(); i++) {
            newHeight += lineList.get(i).getHeight();
        }
        newHeight += (lineList.size()-1) * lineSpace;
        height = Math.max(newHeight, height);
        setMeasuredDimension(width, height);
    }

    /**
     * 清除所有数据
     */
    private void clearLine() {
        lineList.clear();
        line = new Line();
    }

    /**
     * 添加至下一行
     * @param childView 子View
     */
    private void addNextLine(View childView) {
        lineList.add(line);
        line = new Line();
        line.addLineView(childView);
    }

    /**
     * 添加控件至行
     * @param childView 控件
     */
    private void addToLine(View childView){
        if(line == null){
            line = new Line();
        }
        line.addLineView(childView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l = l + getPaddingLeft();
        t = t + getPaddingTop();
        for(int i = 0;i < lineList.size(); i++ ){ //对每一行进行处理
            Line onLine = lineList.get(i);
            if(i > 0){
                t += onLine.getHeight()+lineSpace;
            }
            ArrayList<View> viewList = onLine.getViewList();
            int remainSpacing = getLineRemineSpace(onLine);
            float preSpaceing = remainSpacing/viewList.size();//每行额外需要分配的长度

            for(int j = 0; j < viewList.size(); j++){
                View childView = viewList.get(j);
                int widthMeasureSpce = MeasureSpec.makeMeasureSpec((int)
                        (childView.getMeasuredWidth() + preSpaceing), MeasureSpec.EXACTLY);
                int heightMeasureSpce = MeasureSpec.makeMeasureSpec(
                        childView.getMeasuredHeight(),MeasureSpec.EXACTLY);
                childView.measure(widthMeasureSpce,heightMeasureSpce);
                if(j == 0){
                    childView.layout(l, t, l + childView.getMeasuredWidth(), t + childView.getMeasuredHeight());
                }else{
                    //摆放后一个 需要获取前一个
                    View preView = viewList.get(j - 1);
                    int left = preView.getRight()+(int)horizontalSpace;
                    childView.layout(left,t,left+childView.getMeasuredWidth(),t+childView.getMeasuredHeight());
                }
            }
        }
    }

    /**
     * 获取一行没有填满 空余的部分
     * @param line 行
     * @return 空余部分长度
     */
    private int getLineRemineSpace(Line line){
        return getMeasuredWidth() - getPaddingRight() - getPaddingLeft() - line.getWidth();
    }

    /**
     * 行类
     */
    class Line {
        private int width,height;
        private ArrayList<View> viewList = new ArrayList<>();

        public void addLineView(View view) {
            if (!viewList.contains(view)) {
                viewList.add(view);
                if (viewList.size() == 1) {
                    width = view.getMeasuredWidth();
                } else {
                    width += view.getMeasuredWidth() + horizontalSpace;
                }
                //更新高度
                height = Math.max(height, view.getMeasuredHeight());
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        /**
         * 获取当前行所有TextView
         * @return 当前行子View
         */
        public ArrayList<View> getViewList(){
            return viewList;
        }
    }

    /**
     * 设置水平间距
     * @param space 距离
     */
    @SuppressWarnings("unused")
    public void setHorizontalSpace(int space) {
        if (this.horizontalSpace > 0) {
            this.horizontalSpace = space;
        }
    }
}
