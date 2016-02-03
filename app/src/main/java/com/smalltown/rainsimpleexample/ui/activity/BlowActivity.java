package com.smalltown.rainsimpleexample.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.global.Parameter;

public class BlowActivity extends Activity {
	static TextView text;
	Button btn;
	Button btn1;
	static int i=0;
	RecordThread tt=null;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blow);
		text=(TextView)findViewById(R.id.tv_text);
		btn=(Button)findViewById(R.id.btn_ready);
		btn.setOnClickListener(listener);

	}

	public class MyHandler extends Handler{

	}

	OnClickListener listener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			tt=new RecordThread(r);           //点击按钮，启动线程
			tt.start();
		}

	};

	MyHandler r=new MyHandler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);          //接收到message后更新UI，并通过isblow停止线程
			text.setText("你吹了一下屏幕"+i);
			Parameter.isblow=false;

		}
	};
}