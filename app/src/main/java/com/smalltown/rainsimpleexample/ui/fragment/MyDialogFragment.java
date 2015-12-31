package com.smalltown.rainsimpleexample.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.util.ToastUtil;

/**
 * Created by Diagrams on 2015/12/31 11:07
 */
public class MyDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = View.inflate(getActivity(), R.layout.fragment_dialogtest,null);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast("取消");
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                      public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast("确定");
                    }
                })
                .create();
        return alertDialog;
    }
}
