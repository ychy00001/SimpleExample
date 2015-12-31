package com.smalltown.rainsimpleexample.util;

import android.widget.Toast;
import com.smalltown.rainsimpleexample.global.RainApplication;

/**
 * 吐司显示
 * Created by zh on 2015/10/9.
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToast(final String str) {
        CommonUtil.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(RainApplication.getContext(), str, Toast.LENGTH_SHORT);
                }
                toast.setText(str);
                toast.show();
            }
        });
    }
}
