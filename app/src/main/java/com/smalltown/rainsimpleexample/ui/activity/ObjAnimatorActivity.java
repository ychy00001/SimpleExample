package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import butterknife.Bind;
import butterknife.OnClick;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

/**
 * 动画集合
 * Created by Diagrams on 2015/12/31 16:34
 */
public class ObjAnimatorActivity extends BaseActivity {

    @Bind(R.id.view1)View mView1;
    @Bind(R.id.view2)View mView2;
    @Bind(R.id.view3)View mView3;

    @Override
    protected int initView() {
        return R.layout.activity_objanimator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_trans)
    void clickTrans(){
        ObjectAnimator.ofFloat(mView1,"translationX",0f,100f,0f).setDuration(1000).start();
    }

    @OnClick(R.id.btn_rote)
    void clickRotate(){
        ObjectAnimator.ofFloat(mView2,"rotationX",0f,360f).setDuration(1000).start();
        ObjectAnimator.ofFloat(mView2,"rotationY",0f,360f).setDuration(1000).start();
    }

    @OnClick(R.id.btn_scal)
    void clickScal(){
//        ObjectAnimator.ofFloat(mView3,"scaleX",0f,2.0f).setDuration(1000).start();

//        ScaleAnimation sa = new ScaleAnimation(0f,1.0f,0f,1.0f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF, 0.0f);
//        sa.setDuration(1000);
//        mView3.startAnimation(sa);

        //  import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
        animate(mView3).setDuration(500).rotationYBy(360);
    }

    @OnClick(R.id.btn_all)
    void clickAll(){
        long daily = 0;
        long duration = 200;
        long YLength = 600;
        List<Animator> animators = new ArrayList<>();
        //右下
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mView1, "translationX", 0f, 500f).setDuration(duration);
        ObjectAnimator oa1_y = ObjectAnimator.ofFloat(mView1, "translationY", 0f, YLength).setDuration(duration);
        oa1.setStartDelay(daily);
        oa1_y.setStartDelay(daily);
        setInterpora(oa1, oa1_y);
        daily+= duration;

        //右上
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(mView1, "translationX", 0f, 500f).setDuration(duration);
        ObjectAnimator oa2_y = ObjectAnimator.ofFloat(mView1, "translationY", 0f, -YLength).setDuration(duration);
        oa2.setStartDelay(daily);
        oa2_y.setStartDelay(daily);
        setInterpora(oa2, oa2_y);
        daily+= duration;

        //正下
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(mView1, "translationX", 0f, 0f).setDuration(duration);
        ObjectAnimator oa3_y = ObjectAnimator.ofFloat(mView1, "translationY", 0f, YLength).setDuration(duration);
        oa3.setStartDelay(daily);
        oa3_y.setStartDelay(daily);
        setInterpora(oa3, oa3_y);
        daily+= duration;

        //正上
        ObjectAnimator oa4 = ObjectAnimator.ofFloat(mView1, "translationX", 0f, 0f).setDuration(duration);
        ObjectAnimator oa4_y = ObjectAnimator.ofFloat(mView1, "translationY", 0f, -YLength).setDuration(duration);
        oa4.setStartDelay(daily);
        oa4_y.setStartDelay(daily);
        setInterpora(oa4, oa4_y);
        daily+= duration;

        //左下
        ObjectAnimator oa5 = ObjectAnimator.ofFloat(mView1, "translationX", 0f, -500f).setDuration(duration);
        ObjectAnimator oa5_y = ObjectAnimator.ofFloat(mView1, "translationY", 0f, YLength).setDuration(duration);
        oa5.setStartDelay(daily);
        oa5_y.setStartDelay(daily);
        setInterpora(oa5, oa5_y);
        daily+= duration;

        //左上
        ObjectAnimator oa6 = ObjectAnimator.ofFloat(mView1, "translationX", 0f, -500f).setDuration(duration);
        ObjectAnimator oa6_y = ObjectAnimator.ofFloat(mView1, "translationY", 0f, -YLength).setDuration(duration);
        oa6.setStartDelay(daily);
        oa6_y.setStartDelay(daily);
        setInterpora(oa6, oa6_y);

        ObjectAnimator oa7 = ObjectAnimator.ofFloat(mView2, "rotationX", 0f, 360f).setDuration(daily);
        ObjectAnimator oa8 = ObjectAnimator.ofFloat(mView2, "rotationY", 0f, 360f).setDuration(daily);

        ViewHelper.setScaleX(mView3, 0f);
        ObjectAnimator oa9 = ObjectAnimator.ofFloat(mView3, "scaleX", 0f, 1.5f,0f,1f).setDuration(1000);
//        oa9.setRepeatCount(ValueAnimator.INFINITE);

        animators.add(oa1);
        animators.add(oa2);
        animators.add(oa3);
        animators.add(oa4);
        animators.add(oa5);
        animators.add(oa6);
        animators.add(oa1_y);
        animators.add(oa2_y);
        animators.add(oa3_y);
        animators.add(oa4_y);
        animators.add(oa5_y);
        animators.add(oa6_y);

        animators.add(oa7);
        animators.add(oa8);
        animators.add(oa9 );



        AnimatorSet as = new AnimatorSet();
        as.playTogether(animators);
        as.start();
    }

    private void setInterpora(ObjectAnimator oa1, ObjectAnimator oa1_y) {
        oa1_y.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setInterpolator(new AccelerateDecelerateInterpolator());
    }
}
