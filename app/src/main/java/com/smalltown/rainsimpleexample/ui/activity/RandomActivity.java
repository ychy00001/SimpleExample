package com.smalltown.rainsimpleexample.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.view.FlowLayout;
import com.smalltown.rainsimpleexample.util.ColorUtil;
import com.smalltown.rainsimpleexample.util.CommonUtil;
import com.smalltown.rainsimpleexample.util.DrawableUtil;
import com.smalltown.rainsimpleexample.util.ToastUtil;

import java.util.Random;

/**
 * Created by Diagrams on 2015/12/30 13:40
 */
public class RandomActivity extends BaseActivity {

    private FlowLayout flowLayout;
    private ScrollView scrollView;
    private int textHPadding;
    private int textVPadding;
    private float hotRadios;

    @Override
    protected int initView() {
        return R.layout.activity_random;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollView = (ScrollView) findViewById(R.id.ll_content);
        flowLayout = (FlowLayout) findViewById(R.id.flowlayout);
//        flowLayout = new FlowLayout(this);
//        scrollView.addView(flowLayout);
        initChildView();
    }

    private void initChildView() {
        textHPadding = (int) CommonUtil.getDimens(R.dimen.hot_text_h_padding);
        textVPadding = (int) CommonUtil.getDimens(R.dimen.hot_text_v_padding);
        hotRadios =  CommonUtil.getDimens(R.dimen.hot_corner_radius);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int num = random.nextInt(10);
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setText(texts[num]);
            textView.setTextSize(16);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundColor(ColorUtil.randomColor());
            textView.setBackgroundDrawable(DrawableUtil.generateSeleter(DrawableUtil.generateDrawable(hotRadios, ColorUtil.randomColor()), DrawableUtil.generateDrawable(hotRadios, ColorUtil.randomColor())));
            textView.setPadding(textHPadding, textVPadding, textHPadding, textVPadding);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(((TextView) v).getText().toString());
                }
            });
            flowLayout.addView(textView);
        }
    }

    private String[] texts = {"啊","啊啊","啊啊啊","啊啊啊啊",
            "啊啊","啊啊啊","嘿嘿嘿嘿",
            "嘎嘎嘎嘎","哈哈","啊啊啊啊啊啊啊啊啊啊啊啊啊啊"};
}
